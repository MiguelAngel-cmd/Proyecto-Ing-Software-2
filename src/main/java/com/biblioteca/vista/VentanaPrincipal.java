package com.biblioteca.vista;

import com.biblioteca.excepcion.BibliotecaException;
import com.biblioteca.modelo.Prestamo;
import com.biblioteca.modelo.Recurso;
import com.biblioteca.modelo.TipoRecurso;
import com.biblioteca.modelo.Usuario;
import com.biblioteca.multas.PoliticaMulta;
import com.biblioteca.servicio.BibliotecaFachada;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/** Interfaz gráfica sencilla. Igual que la consola, solo habla con la fachada. */
public class VentanaPrincipal extends JFrame {
    private final BibliotecaFachada fachada;
    private final DefaultTableModel modeloRecursos = new DefaultTableModel(new String[]{"Código", "Tipo", "Título", "Estado", "Detalle"}, 0);
    private final DefaultTableModel modeloPrestamos = new DefaultTableModel(new String[]{"N°", "Recurso", "Usuario", "Vence"}, 0);
    private final JTable tablaRecursos = new JTable(modeloRecursos);
    private final JTable tablaPrestamos = new JTable(modeloPrestamos);
    private final JLabel estado = new JLabel();

    public VentanaPrincipal(BibliotecaFachada fachada) {
        super("Biblioteca y préstamo de recursos");
        this.fachada = fachada;

        tablaRecursos.setDefaultEditor(Object.class, null);       // tablas de solo lectura
        tablaPrestamos.setDefaultEditor(Object.class, null);
        JPanel tablas = new JPanel(new GridLayout(2, 1));
        tablas.add(new JScrollPane(tablaRecursos));
        tablas.add(new JScrollPane(tablaPrestamos));

        JPanel botones = new JPanel(new GridLayout(2, 4));
        agregar(botones, "Prestar", this::prestar);
        agregar(botones, "Devolver", this::devolver);
        agregar(botones, "Renovar", this::renovar);
        agregar(botones, "Lista de espera", this::listaEspera);
        agregar(botones, "Ver avisos", this::verAvisos);
        agregar(botones, "Registrar recurso", this::registrar);
        agregar(botones, "Política de multas", this::politica);
        agregar(botones, "Adelantar fecha", this::adelantarFecha);

        add(estado, BorderLayout.NORTH);
        add(tablas, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
        setSize(820, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        refrescar();
    }

    private void agregar(JPanel panel, String texto, Runnable accion) {
        JButton boton = new JButton(texto);
        boton.addActionListener(e -> {
            try {
                accion.run();
            } catch (BibliotecaException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo completar", JOptionPane.WARNING_MESSAGE);
            }
            refrescar();
        });
        panel.add(boton);
    }

    private void prestar() {
        Prestamo p = fachada.prestar(recursoSeleccionado(), elegirUsuario("¿A quién se presta?"));
        JOptionPane.showMessageDialog(this, "Préstamo #" + p.getId() + ". Vence el " + p.getFechaVencimiento());
    }

    private void devolver() {
        Prestamo p = fachada.devolver(prestamoSeleccionado());
        JOptionPane.showMessageDialog(this, p.getMulta() > 0 ? "Devuelto con multa de $" + p.getMulta() : "Devuelto a tiempo: sin multa.");
    }

    private void renovar() {
        Prestamo p = fachada.renovar(prestamoSeleccionado());
        JOptionPane.showMessageDialog(this, "Renovado. Nuevo vencimiento: " + p.getFechaVencimiento());
    }

    private void listaEspera() {
        fachada.unirseListaEspera(recursoSeleccionado(), elegirUsuario("¿Quién espera el recurso?"));
        JOptionPane.showMessageDialog(this, "Agregado a la lista de espera.");
    }

    private void verAvisos() {
        String id = elegirUsuario("¿Avisos de quién?");
        JOptionPane.showMessageDialog(this, fachada.avisos(id).isEmpty() ? "No tiene avisos." : String.join("\n", fachada.avisos(id)));
        fachada.limpiarAvisos(id);
    }

    private void registrar() {
        TipoRecurso tipo = (TipoRecurso) JOptionPane.showInputDialog(this, "Tipo de recurso", "Registrar",
                JOptionPane.QUESTION_MESSAGE, null, TipoRecurso.values(), TipoRecurso.LIBRO);
        if (tipo == null) return;
        fachada.registrarRecurso(tipo, JOptionPane.showInputDialog(this, "Título"),
                JOptionPane.showInputDialog(this, "Autor / marca / categoría"));
    }

    private void politica() {
        Object elegida = JOptionPane.showInputDialog(this, "Política de multas", "Política", JOptionPane.QUESTION_MESSAGE,
                null, fachada.politicasDisponibles().toArray(), fachada.politicaActual());
        if (elegida != null) fachada.cambiarPolitica(fachada.politicasDisponibles().indexOf(elegida));
    }

    private void adelantarFecha() {
        String dias = JOptionPane.showInputDialog(this, "Días a adelantar (para probar multas)");
        if (dias != null) fachada.avanzarDias(Integer.parseInt(dias.trim()));
    }

    private String elegirUsuario(String pregunta) {
        Usuario u = (Usuario) JOptionPane.showInputDialog(this, pregunta, "Usuario", JOptionPane.QUESTION_MESSAGE,
                null, fachada.listarUsuarios().toArray(), null);
        if (u == null) throw new BibliotecaException("Operación cancelada.");
        return u.getId();
    }

    private String recursoSeleccionado() {
        int fila = tablaRecursos.getSelectedRow();
        if (fila < 0) throw new BibliotecaException("Seleccione un recurso de la tabla de arriba.");
        return (String) modeloRecursos.getValueAt(fila, 0);
    }

    private int prestamoSeleccionado() {
        int fila = tablaPrestamos.getSelectedRow();
        if (fila < 0) throw new BibliotecaException("Seleccione un préstamo de la tabla de abajo.");
        return (Integer) modeloPrestamos.getValueAt(fila, 0);
    }

    private void refrescar() {
        estado.setText("  Fecha del sistema: " + fachada.hoy() + "   |   Política de multas: " + fachada.politicaActual().getNombre());
        modeloRecursos.setRowCount(0);
        for (Recurso r : fachada.listarRecursos())
            modeloRecursos.addRow(new Object[]{r.getId(), r.getTipo(), r.getTitulo(), r.getEstado(), r.getEtiquetaDetalle() + ": " + r.getDetalle()});
        modeloPrestamos.setRowCount(0);
        for (Prestamo p : fachada.prestamosActivos())
            modeloPrestamos.addRow(new Object[]{p.getId(), p.getRecurso().getTitulo(), p.getUsuario().getNombre(), p.getFechaVencimiento()});
    }
}
