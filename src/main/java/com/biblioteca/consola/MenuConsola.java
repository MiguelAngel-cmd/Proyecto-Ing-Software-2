package com.biblioteca.consola;

import com.biblioteca.excepcion.BibliotecaException;
import com.biblioteca.modelo.Prestamo;
import com.biblioteca.modelo.Recurso;
import com.biblioteca.modelo.TipoRecurso;
import com.biblioteca.modelo.Usuario;
import com.biblioteca.multas.PoliticaMulta;
import com.biblioteca.servicio.BibliotecaFachada;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Interfaz de consola. No tiene lógica de negocio: pide datos, llama a la fachada y muestra el resultado. */
public class MenuConsola {
    private final BibliotecaFachada fachada;
    private final Scanner entrada;
    private final PrintStream salida;

    public MenuConsola(BibliotecaFachada fachada, InputStream entrada, PrintStream salida) {
        this.fachada = fachada;
        this.entrada = new Scanner(entrada);
        this.salida = salida;
    }

    public void iniciar() {
        try {
            boolean seguir = true;
            while (seguir) {
                mostrarMenu();
                seguir = ejecutar(leer("Opción"));
            }
            salida.println("Hasta pronto.");
        } catch (NoSuchElementException finDeEntrada) {
            salida.println("\nFin de la entrada. Hasta pronto.");
        }
    }

    private static final String[] OPCIONES = {"Ver catálogo", "Registrar recurso", "Modificar recurso",
            "Dar de baja un recurso", "Prestar un recurso", "Devolver un recurso", "Renovar un préstamo",
            "Ver préstamos activos", "Unirse a la lista de espera", "Ver avisos de un usuario",
            "Cambiar la política de multas", "Adelantar la fecha (para probar multas)"};

    private void mostrarMenu() {
        salida.println("\n===== BIBLIOTECA =====   fecha del sistema: " + fachada.hoy());
        for (int i = 0; i < OPCIONES.length; i++) salida.println(String.format("%2d. %s", i + 1, OPCIONES[i]));
        salida.println(" 0. Salir");
    }

    private boolean ejecutar(String opcion) {
        if (opcion.equals("0")) return false;
        try {
            switch (opcion) {
                case "1" -> verCatalogo();
                case "2" -> registrarRecurso();
                case "3" -> modificarRecurso();
                case "4" -> salida.println(fachada.darDeBajaRecurso(leer("Código del recurso")).getEstado());
                case "5" -> prestar();
                case "6" -> devolver();
                case "7" -> renovar();
                case "8" -> verPrestamos();
                case "9" -> unirseAEspera();
                case "10" -> verAvisos();
                case "11" -> cambiarPolitica();
                case "12" -> adelantarFecha();
                default -> salida.println("Opción no válida.");
            }
        } catch (BibliotecaException e) {
            salida.println("ERROR: " + e.getMessage());
        } catch (NumberFormatException e) {
            salida.println("ERROR: debe escribir un número.");
        }
        return true;
    }

    private void verCatalogo() {
        salida.println(String.format("%-6s %-8s %-28s %-11s %s", "CÓD", "TIPO", "TÍTULO", "ESTADO", "DETALLE"));
        for (Recurso r : fachada.listarRecursos())
            salida.println(String.format("%-6s %-8s %-28s %-11s %s: %s", r.getId(), r.getTipo(), r.getTitulo(),
                    r.getEstado(), r.getEtiquetaDetalle(), r.getDetalle()));
    }

    private void registrarRecurso() {
        TipoRecurso tipo = switch (leer("Tipo (1 Libro, 2 Equipo, 3 Otro)")) {
            case "1" -> TipoRecurso.LIBRO;
            case "2" -> TipoRecurso.EQUIPO;
            case "3" -> TipoRecurso.OTRO;
            default -> throw new BibliotecaException("Tipo no válido.");
        };
        Recurso r = fachada.registrarRecurso(tipo, leer("Título"), leer("Autor / marca / categoría"));
        salida.println("Registrado: " + r.getId() + " - " + r.getTitulo());
    }

    private void modificarRecurso() {
        Recurso r = fachada.modificarRecurso(leer("Código del recurso"), leer("Nuevo título"), leer("Nuevo autor / marca / categoría"));
        salida.println("Modificado: " + r.getId() + " - " + r.getTitulo());
    }

    private void prestar() {
        mostrarUsuarios();
        String idUsuario = leer("Código del usuario");
        Prestamo p = fachada.prestar(leer("Código del recurso"), idUsuario);
        salida.println("Préstamo #" + p.getId() + ": " + p.getRecurso().getTitulo() + " a "
                + p.getUsuario().getNombre() + ". Vence el " + p.getFechaVencimiento() + ".");
    }

    private void devolver() {
        Prestamo p = fachada.devolver(Integer.parseInt(leer("Número de préstamo")));
        salida.println("Devuelto: " + p.getRecurso().getTitulo() + " (" + p.getUsuario().getNombre() + ").");
        salida.println(p.getMulta() > 0 ? "Multa: " + pesos(p.getMulta()) + " (" + fachada.politicaActual().getNombre() + ")"
                                       : "Devuelto a tiempo: sin multa.");
    }

    private void renovar() {
        Prestamo p = fachada.renovar(Integer.parseInt(leer("Número de préstamo")));
        salida.println("Renovado. Nuevo vencimiento: " + p.getFechaVencimiento() + ".");
    }

    private void verPrestamos() {
        List<Prestamo> activos = fachada.prestamosActivos();
        if (activos.isEmpty()) salida.println("No hay préstamos activos.");
        for (Prestamo p : activos) {
            long retraso = p.diasRetraso(fachada.hoy());
            salida.println(String.format("#%-3d %-6s %-26s %-12s vence %s%s", p.getId(), p.getRecurso().getId(),
                    p.getRecurso().getTitulo(), p.getUsuario().getNombre(), p.getFechaVencimiento(),
                    retraso > 0 ? "  VENCIDO (" + retraso + " d)" : ""));
        }
    }

    private void unirseAEspera() {
        mostrarUsuarios();
        String idUsuario = leer("Código del usuario que espera");
        String idRecurso = leer("Código del recurso prestado");
        fachada.unirseListaEspera(idRecurso, idUsuario);
        salida.println("Agregado. Personas en la fila: " + fachada.cantidadEnEspera(idRecurso) + ".");
    }

    private void verAvisos() {
        mostrarUsuarios();
        String idUsuario = leer("Código del usuario");
        List<String> avisos = fachada.avisos(idUsuario);
        if (avisos.isEmpty()) salida.println("No tiene avisos.");
        avisos.forEach(a -> salida.println(" * " + a));
        fachada.limpiarAvisos(idUsuario);
    }

    private void cambiarPolitica() {
        List<PoliticaMulta> politicas = fachada.politicasDisponibles();
        for (int i = 0; i < politicas.size(); i++)
            salida.println("  " + (i + 1) + ". " + politicas.get(i).getNombre()
                    + (politicas.get(i) == fachada.politicaActual() ? "   <-- actual" : ""));
        fachada.cambiarPolitica(Integer.parseInt(leer("Número de la política")) - 1);
        salida.println("Política activa: " + fachada.politicaActual().getNombre());
    }

    private void adelantarFecha() {
        fachada.avanzarDias(Integer.parseInt(leer("Días a adelantar")));
        salida.println("La fecha del sistema ahora es " + fachada.hoy() + ".");
    }

    private void mostrarUsuarios() {
        salida.print("Usuarios:");
        for (Usuario u : fachada.listarUsuarios()) salida.print("  " + u.getId() + " (" + u.getNombre() + ")");
        salida.println();
    }

    private String leer(String etiqueta) {
        salida.print(etiqueta + ": ");
        salida.flush();
        return entrada.nextLine().trim();
    }

    private static String pesos(long valor) {
        return String.format(Locale.GERMANY, "$%,d", valor);       // 5000 -> $5.000
    }
}
