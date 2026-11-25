package com.biblioteca.servicio;

import com.biblioteca.catalogo.Catalogo;
import com.biblioteca.espera.ListaEspera;
import com.biblioteca.excepcion.BibliotecaException;
import com.biblioteca.modelo.Prestamo;
import com.biblioteca.modelo.Recurso;
import com.biblioteca.modelo.ReglaPrestamo;
import com.biblioteca.modelo.Usuario;
import com.biblioteca.multas.PoliticaMulta;
import com.biblioteca.util.Reloj;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Casos de uso del préstamo. Aquí trabajan juntos los patrones:
 * Singleton (Catalogo), Strategy (la política de multa vigente) y Observer (las listas de espera).
 */
public class PrestamoServicio {
    private final Catalogo catalogo;
    private final Reloj reloj;
    private final Map<Integer, Prestamo> prestamos = new TreeMap<>();
    private final Map<String, ListaEspera> esperas = new HashMap<>();
    private PoliticaMulta politica = PoliticaMulta.TODAS.get(0);       // Strategy: la política vigente
    private int contador;

    public PrestamoServicio(Catalogo catalogo, Reloj reloj) {
        this.catalogo = catalogo;
        this.reloj = reloj;
    }

    public synchronized Prestamo prestar(String idRecurso, Usuario usuario) {
        Recurso recurso = catalogo.obtener(idRecurso);
        if (recurso.estaDeBaja()) throw new BibliotecaException("'" + recurso.getTitulo() + "' está dado de baja.");
        if (!recurso.estaDisponible())
            throw new BibliotecaException("'" + recurso.getTitulo() + "' está prestado. Puede unirse a la lista de espera.");

        ReglaPrestamo regla = recurso.getRegla();
        long activos = activosDe(usuario.getId()).stream()
                .filter(p -> p.getRecurso().getTipo() == recurso.getTipo()).count();
        if (activos >= regla.maxPorUsuario())
            throw new BibliotecaException("Límite alcanzado: " + usuario.getNombre() + " ya tiene " + activos
                    + " préstamo(s) de tipo " + recurso.getTipo() + " (máximo " + regla.maxPorUsuario() + ").");

        recurso.marcarPrestado();
        listaDe(recurso).cancelar(usuario.getId());          // si esperaba este recurso, ya no hace falta
        Prestamo prestamo = new Prestamo(++contador, recurso, usuario, reloj.hoy().plusDays(regla.plazoDias()));
        prestamos.put(prestamo.getId(), prestamo);
        return prestamo;
    }

    public synchronized Prestamo devolver(int idPrestamo) {
        Prestamo prestamo = obtenerActivo(idPrestamo);
        Recurso recurso = prestamo.getRecurso();
        long dias = prestamo.diasRetraso(reloj.hoy());
        long multa = dias > 0 ? politica.calcular(dias, recurso) : 0;       // Strategy
        prestamo.registrarDevolucion(reloj.hoy(), multa);
        recurso.marcarDisponible();
        listaDe(recurso).notificarSiguiente();                               // Observer
        return prestamo;
    }

    public synchronized Prestamo renovar(int idPrestamo) {
        Prestamo prestamo = obtenerActivo(idPrestamo);
        Recurso recurso = prestamo.getRecurso();
        ReglaPrestamo regla = recurso.getRegla();
        if (prestamo.getRenovaciones() >= regla.maxRenovaciones())
            throw new BibliotecaException("No se puede renovar: ya usó las " + regla.maxRenovaciones()
                    + " renovación(es) permitidas para " + recurso.getTipo() + ".");
        if (prestamo.diasRetraso(reloj.hoy()) > 0)
            throw new BibliotecaException("No se puede renovar: el préstamo está vencido. Devuélvalo.");
        if (listaDe(recurso).tamano() > 0)
            throw new BibliotecaException("No se puede renovar: hay usuarios en la lista de espera.");
        prestamo.renovar(regla.plazoDias());
        return prestamo;
    }

    public synchronized void unirseListaEspera(String idRecurso, Usuario usuario) {
        Recurso recurso = catalogo.obtener(idRecurso);
        if (recurso.estaDeBaja()) throw new BibliotecaException("'" + recurso.getTitulo() + "' está dado de baja.");
        if (recurso.estaDisponible())
            throw new BibliotecaException("'" + recurso.getTitulo() + "' está disponible; no hace falta esperar.");
        if (activosDe(usuario.getId()).stream().anyMatch(p -> p.getRecurso() == recurso))
            throw new BibliotecaException("Usted ya tiene prestado este recurso.");
        listaDe(recurso).suscribir(usuario);                                // Observer: el usuario es el observador
    }

    public synchronized int cantidadEnEspera(String idRecurso) {
        return listaDe(catalogo.obtener(idRecurso)).tamano();
    }

    public synchronized List<Prestamo> activos() {
        return prestamos.values().stream().filter(Prestamo::estaActivo).toList();
    }

    public synchronized List<Prestamo> activosDe(String idUsuario) {
        return activos().stream().filter(p -> p.getUsuario().getId().equals(idUsuario)).toList();
    }

    public PoliticaMulta getPolitica() { return politica; }
    public void setPolitica(PoliticaMulta nueva) { this.politica = nueva; }

    private ListaEspera listaDe(Recurso recurso) {
        return esperas.computeIfAbsent(recurso.getId(), id -> new ListaEspera(recurso));
    }

    private Prestamo obtenerActivo(int idPrestamo) {
        Prestamo prestamo = prestamos.get(idPrestamo);
        if (prestamo == null) throw new BibliotecaException("No existe el préstamo número " + idPrestamo + ".");
        if (!prestamo.estaActivo()) throw new BibliotecaException("El préstamo " + idPrestamo + " ya fue devuelto.");
        return prestamo;
    }
}
