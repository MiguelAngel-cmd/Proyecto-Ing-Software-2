package com.biblioteca.servicio;

import com.biblioteca.catalogo.Catalogo;
import com.biblioteca.dao.PrestamoDAO;
import com.biblioteca.espera.AvisoUsuario;
import com.biblioteca.espera.GestorListasEspera;
import com.biblioteca.excepcion.BibliotecaException;
import com.biblioteca.modelo.EstadoRecurso;
import com.biblioteca.modelo.Prestamo;
import com.biblioteca.modelo.Recurso;
import com.biblioteca.modelo.Usuario;
import com.biblioteca.multas.CalculadoraMultas;
import com.biblioteca.reglas.ReglaPrestamo;
import com.biblioteca.util.Reloj;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Lógica de negocio de los préstamos (RF-02, RF-03, RF-04, RF-05, RF-06).
 * Aquí se juntan los patrones: el catálogo (Singleton), la regla de cada recurso,
 * la calculadora de multas (Strategy) y la lista de espera (Observer).
 */
public class PrestamoServicio {

    private final Catalogo catalogo;
    private final PrestamoDAO prestamos;
    private final CalculadoraMultas multas;
    private final GestorListasEspera espera;
    private final Reloj reloj;

    public PrestamoServicio(Catalogo catalogo, PrestamoDAO prestamos, CalculadoraMultas multas,
                            GestorListasEspera espera, Reloj reloj) {
        this.catalogo = catalogo;
        this.prestamos = prestamos;
        this.multas = multas;
        this.espera = espera;
        this.reloj = reloj;
    }

    /** RF-02: presta un recurso a un usuario validando disponibilidad y reglas del tipo. */
    public synchronized Prestamo prestar(String idRecurso, Usuario usuario) {
        Recurso recurso = catalogo.obtener(idRecurso);
        if (recurso.getEstado() == EstadoRecurso.DE_BAJA) {
            throw new BibliotecaException("El recurso '" + recurso.getTitulo() + "' está dado de baja.");
        }
        if (!recurso.estaDisponible()) {
            throw new BibliotecaException("El recurso '" + recurso.getTitulo()
                    + "' está prestado. Puede unirse a la lista de espera.");
        }
        ReglaPrestamo regla = recurso.getRegla();
        long activos = prestamosActivosDe(usuario.getId()).stream()
                .filter(p -> p.getRecurso().getTipo() == recurso.getTipo())
                .count();
        if (activos >= regla.getMaxPrestamosPorUsuario()) {
            throw new BibliotecaException("Límite alcanzado: " + usuario.getNombre() + " ya tiene " + activos
                    + " préstamo(s) de tipo " + recurso.getTipo() + " (máximo "
                    + regla.getMaxPrestamosPorUsuario() + ").");
        }
        recurso.marcarPrestado();
        espera.salir(recurso, usuario.getId());           // si estaba esperando, ya no hace falta
        LocalDate hoy = reloj.hoy();
        Prestamo prestamo = new Prestamo(prestamos.siguienteId(), recurso, usuario, hoy,
                hoy.plusDays(regla.getPlazoDias()));
        prestamos.guardar(prestamo);
        return prestamo;
    }

    /** RF-03 y RF-05: registra la devolución, calcula la multa y avisa al siguiente de la lista (RF-06). */
    public synchronized Prestamo devolver(int idPrestamo) {
        Prestamo prestamo = obtenerActivo(idPrestamo);
        Recurso recurso = prestamo.getRecurso();
        long dias = prestamo.diasRetraso(reloj.hoy());
        long multa = multas.calcular(dias, recurso);
        prestamo.registrarDevolucion(reloj.hoy(), multa);
        prestamos.guardar(prestamo);
        recurso.marcarDisponible();
        espera.notificarDisponible(recurso);
        return prestamo;
    }

    /** RF-04: renueva un préstamo respetando el máximo de renovaciones de su tipo. */
    public synchronized Prestamo renovar(int idPrestamo) {
        Prestamo prestamo = obtenerActivo(idPrestamo);
        Recurso recurso = prestamo.getRecurso();
        ReglaPrestamo regla = recurso.getRegla();
        if (prestamo.getRenovaciones() >= regla.getMaxRenovaciones()) {
            throw new BibliotecaException("No se puede renovar: ya usó las " + regla.getMaxRenovaciones()
                    + " renovación(es) permitidas para " + recurso.getTipo() + ".");
        }
        if (prestamo.diasRetraso(reloj.hoy()) > 0) {
            throw new BibliotecaException("No se puede renovar: el préstamo está vencido. Devuélvalo.");
        }
        if (espera.cantidadEnEspera(recurso) > 0) {
            throw new BibliotecaException("No se puede renovar: hay usuarios en la lista de espera.");
        }
        prestamo.renovar(regla.getPlazoDias());
        prestamos.guardar(prestamo);
        return prestamo;
    }

    /** Un usuario entra a la lista de espera de un recurso que está prestado. */
    public synchronized void unirseListaEspera(String idRecurso, Usuario usuario) {
        Recurso recurso = catalogo.obtener(idRecurso);
        if (recurso.estaDisponible()) {
            throw new BibliotecaException("El recurso '" + recurso.getTitulo()
                    + "' está disponible; no hace falta esperar.");
        }
        if (recurso.getEstado() == EstadoRecurso.DE_BAJA) {
            throw new BibliotecaException("El recurso '" + recurso.getTitulo() + "' está dado de baja.");
        }
        boolean loTieneElMismoUsuario = prestamosActivosDe(usuario.getId()).stream()
                .anyMatch(p -> p.getRecurso().getId().equals(recurso.getId()));
        if (loTieneElMismoUsuario) {
            throw new BibliotecaException("Usted ya tiene prestado este recurso.");
        }
        espera.unirse(recurso, new AvisoUsuario(usuario));
    }

    public int cantidadEnEspera(String idRecurso) {
        return espera.cantidadEnEspera(catalogo.obtener(idRecurso));
    }

    public List<Prestamo> historial() {
        return prestamos.listar();
    }

    public List<Prestamo> prestamosActivos() {
        List<Prestamo> activos = new ArrayList<>();
        for (Prestamo p : prestamos.listar()) {
            if (p.estaActivo()) {
                activos.add(p);
            }
        }
        return activos;
    }

    public List<Prestamo> prestamosActivosDe(String idUsuario) {
        List<Prestamo> propios = new ArrayList<>();
        for (Prestamo p : prestamosActivos()) {
            if (p.getUsuario().getId().equals(idUsuario)) {
                propios.add(p);
            }
        }
        return propios;
    }

    public Prestamo obtener(int idPrestamo) {
        return prestamos.buscarPorId(idPrestamo)
                .orElseThrow(() -> new BibliotecaException("No existe el préstamo número " + idPrestamo + "."));
    }

    private Prestamo obtenerActivo(int idPrestamo) {
        Prestamo prestamo = obtener(idPrestamo);
        if (!prestamo.estaActivo()) {
            throw new BibliotecaException("El préstamo " + idPrestamo + " ya fue devuelto.");
        }
        return prestamo;
    }
}
