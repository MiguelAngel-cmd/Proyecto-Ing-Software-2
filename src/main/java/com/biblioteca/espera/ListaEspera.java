package com.biblioteca.espera;

import com.biblioteca.excepcion.BibliotecaException;
import com.biblioteca.modelo.Recurso;
import java.util.ArrayDeque;
import java.util.Deque;

/** Patrón Observer (el sujeto): la fila de espera de un recurso. Al liberarse, avisa al primero. */
public class ListaEspera {
    private final Recurso recurso;
    private final Deque<ObservadorDisponibilidad> cola = new ArrayDeque<>();

    public ListaEspera(Recurso recurso) {
        this.recurso = recurso;
    }

    public synchronized void suscribir(ObservadorDisponibilidad observador) {
        if (contiene(observador.getId()))
            throw new BibliotecaException("Ya está en la lista de espera de '" + recurso.getTitulo() + "'.");
        cola.addLast(observador);
    }

    public synchronized boolean cancelar(String idUsuario) {
        return cola.removeIf(o -> o.getId().equals(idUsuario));
    }

    /** Avisa al primero de la fila y lo saca de ella. Devuelve su código, o null si nadie esperaba. */
    public synchronized String notificarSiguiente() {
        ObservadorDisponibilidad siguiente = cola.pollFirst();
        if (siguiente == null) return null;
        siguiente.recursoDisponible(recurso);
        return siguiente.getId();
    }

    public synchronized int tamano() { return cola.size(); }

    public synchronized boolean contiene(String idUsuario) {
        return cola.stream().anyMatch(o -> o.getId().equals(idUsuario));
    }
}
