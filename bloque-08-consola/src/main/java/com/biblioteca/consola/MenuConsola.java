package com.biblioteca.consola;

import com.biblioteca.dto.PrestamoDTO;
import com.biblioteca.dto.RecursoDTO;
import com.biblioteca.dto.UsuarioDTO;
import com.biblioteca.excepcion.BibliotecaException;
import com.biblioteca.modelo.TipoRecurso;
import com.biblioteca.multas.PoliticaMulta;
import com.biblioteca.servicio.BibliotecaFachada;
import com.biblioteca.util.Dinero;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Menú de consola. Solo lee y escribe texto: toda la lógica la hace la
 * {@link BibliotecaFachada}, igual que la ventana gráfica.
 */
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
            salida.println();
            salida.println("Fin de la entrada. Hasta pronto.");
        }
    }

    private void mostrarMenu() {
        salida.println();
        salida.println("===== BIBLIOTECA =====   fecha del sistema: " + fachada.hoy());
        salida.println(" 1. Ver catálogo");
        salida.println(" 2. Buscar recurso");
        salida.println(" 3. Registrar recurso");
        salida.println(" 4. Modificar recurso");
        salida.println(" 5. Dar de baja un recurso");
        salida.println(" 6. Prestar un recurso");
        salida.println(" 7. Devolver un recurso");
        salida.println(" 8. Renovar un préstamo");
        salida.println(" 9. Ver préstamos activos");
        salida.println("10. Unirse a la lista de espera");
        salida.println("11. Ver avisos de un usuario");
        salida.println("12. Cambiar la política de multas");
        salida.println("13. Adelantar la fecha (para probar multas)");
        salida.println(" 0. Salir");
    }

    /** Ejecuta una opción; devuelve false cuando se quiere salir. */
    private boolean ejecutar(String opcion) {
        if (opcion.equals("0")) {
            return false;
        }
        try {
            switch (opcion) {
                case "1" -> verCatalogo(fachada.listarRecursos());
                case "2" -> verCatalogo(fachada.buscarRecursos(leer("Texto a buscar")));
                case "3" -> registrarRecurso();
                case "4" -> modificarRecurso();
                case "5" -> darDeBaja();
                case "6" -> prestar();
                case "7" -> devolver();
                case "8" -> renovar();
                case "9" -> verPrestamos();
                case "10" -> unirseAEspera();
                case "11" -> verAvisos();
                case "12" -> politicaDeMultas();
                case "13" -> adelantarFecha();
                default -> salida.println("Opción no válida.");
            }
        } catch (BibliotecaException e) {
            salida.println("ERROR: " + e.getMessage());
        } catch (NumberFormatException e) {
            salida.println("ERROR: debe escribir un número.");
        }
        return true;
    }

    // ------------------------------------------------------------ operaciones

    private void verCatalogo(List<RecursoDTO> recursos) {
        if (recursos.isEmpty()) {
            salida.println("No se encontraron recursos.");
            return;
        }
        salida.println(String.format("%-6s %-8s %-32s %-10s %s", "CÓD", "TIPO", "TÍTULO", "ESTADO", "DETALLE"));
        for (RecursoDTO r : recursos) {
            salida.println(r.resumen());
        }
    }

    private void registrarRecurso() {
        salida.println("Tipo: 1) Libro  2) Equipo  3) Otro");
        TipoRecurso tipo = switch (leer("Tipo")) {
            case "1" -> TipoRecurso.LIBRO;
            case "2" -> TipoRecurso.EQUIPO;
            case "3" -> TipoRecurso.OTRO;
            default -> throw new BibliotecaException("Tipo no válido.");
        };
        String titulo = leer("Título");
        String detalle = leer("Autor / marca / categoría");
        RecursoDTO nuevo = fachada.registrarRecurso(tipo, titulo, detalle);
        salida.println("Registrado: " + nuevo.id() + " - " + nuevo.titulo());
    }

    private void modificarRecurso() {
        String id = leer("Código del recurso");
        String titulo = leer("Nuevo título");
        String detalle = leer("Nuevo autor / marca / categoría");
        RecursoDTO r = fachada.modificarRecurso(id, titulo, detalle);
        salida.println("Modificado: " + r.id() + " - " + r.titulo());
    }

    private void darDeBaja() {
        RecursoDTO r = fachada.darDeBajaRecurso(leer("Código del recurso"));
        salida.println(r.id() + " - " + r.titulo() + " quedó " + r.estado() + ".");
    }

    private void prestar() {
        mostrarUsuarios();
        String idUsuario = leer("Código del usuario");
        String idRecurso = leer("Código del recurso");
        PrestamoDTO p = fachada.prestar(idRecurso, idUsuario);
        salida.println("Préstamo #" + p.id() + ": " + p.tituloRecurso() + " a " + p.nombreUsuario()
                + ". Vence el " + p.fechaVencimiento() + ".");
    }

    private void devolver() {
        PrestamoDTO p = fachada.devolver(Integer.parseInt(leer("Número de préstamo")));
        salida.println("Devuelto: " + p.tituloRecurso() + " (" + p.nombreUsuario() + ").");
        if (p.multa() > 0) {
            salida.println("Multa por " + p.diasRetraso() + " día(s) de retraso, política '"
                    + fachada.politicaActual().getNombre() + "': " + Dinero.formato(p.multa()));
        } else {
            salida.println("Devuelto a tiempo: sin multa.");
        }
    }

    private void renovar() {
        PrestamoDTO p = fachada.renovar(Integer.parseInt(leer("Número de préstamo")));
        salida.println("Renovado. Nuevo vencimiento: " + p.fechaVencimiento()
                + " (renovación " + p.renovaciones() + " de " + p.maxRenovaciones() + ").");
    }

    private void verPrestamos() {
        List<PrestamoDTO> activos = fachada.prestamosActivos();
        if (activos.isEmpty()) {
            salida.println("No hay préstamos activos.");
        }
        for (PrestamoDTO p : activos) {
            salida.println(p.resumen());
        }
    }

    private void unirseAEspera() {
        mostrarUsuarios();
        String idUsuario = leer("Código del usuario que espera");
        String idRecurso = leer("Código del recurso prestado");
        fachada.unirseListaEspera(idRecurso, idUsuario);
        salida.println("Agregado a la lista de espera. Personas en la fila: "
                + fachada.cantidadEnEspera(idRecurso) + ".");
    }

    private void verAvisos() {
        mostrarUsuarios();
        String idUsuario = leer("Código del usuario");
        List<String> avisos = fachada.avisos(idUsuario);
        if (avisos.isEmpty()) {
            salida.println("No tiene avisos.");
            return;
        }
        for (String aviso : avisos) {
            salida.println(" * " + aviso);
        }
        fachada.limpiarAvisos(idUsuario);
    }

    private void politicaDeMultas() {
        List<PoliticaMulta> politicas = fachada.politicasDisponibles();
        for (int i = 0; i < politicas.size(); i++) {
            PoliticaMulta p = politicas.get(i);
            String actual = p.getNombre().equals(fachada.politicaActual().getNombre()) ? "   <-- actual" : "";
            salida.println("  " + (i + 1) + ". " + p.getNombre() + ": " + p.getDescripcion() + actual);
        }
        fachada.cambiarPolitica(Integer.parseInt(leer("Número de la política")) - 1);
        salida.println("Política activa: " + fachada.politicaActual().getNombre());
    }

    private void adelantarFecha() {
        fachada.avanzarDias(Integer.parseInt(leer("Días a adelantar")));
        salida.println("La fecha del sistema ahora es " + fachada.hoy() + ".");
    }

    // ---------------------------------------------------------------- ayudas

    private void mostrarUsuarios() {
        salida.print("Usuarios:");
        for (UsuarioDTO u : fachada.listarUsuarios()) {
            salida.print("  " + u.id() + " (" + u.nombre() + ")");
        }
        salida.println();
    }

    private String leer(String etiqueta) {
        salida.print(etiqueta + ": ");
        salida.flush();
        return entrada.nextLine().trim();
    }
}
