package com.biblioteca.espera;

import com.biblioteca.excepcion.BibliotecaException;
import com.biblioteca.modelo.Recurso;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

/**
 * Sujeto del patrón Observer (Subject): cola de espera de UN recurso.
 * Los usuarios se suscriben (entran a la cola) y, cuando el recurso se devuelve,
 * {@link #notificarSiguiente()} avisa solo al primero de la fila (RF-06).
 */
public class ListaEspera {

    private final Recurso recurso;
    private final Deque<ObservadorDisponibilidad> cola = new ArrayDeque<>();

    public ListaEspera(Recurso recurso) {
        this.recurso = recurso;
    }

    public synchronized void suscribir(ObservadorDisponibilidad observador) {
        if (contiene(observador.getIdUsuario())) {
            throw new BibliotecaException("Ya está en la lista de espera de '" + recurso.getTitulo() + "'.");
        }
        cola.addLast(observador);
    }

    /** Saca a un usuario de la cola (por ejemplo, porque ya consiguió el recurso). */
    public synchronized boolean cancelar(String idUsuario) {
        Iterator<ObservadorDisponibilidad> it = cola.iterator();
        while (it.hasNext()) {
            if (it.next().getIdUsuario().equals(idUsuario)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    /** Avisa al primero de la cola y lo saca de ella. Devuelve su código, o null si no había nadie. */
    public synchronized String notificarSiguiente() {
        ObservadorDisponibilidad siguiente = cola.pollFirst();
        if (siguiente == null) {
            return null;
        }
        siguiente.recursoDisponible(recurso);
        return siguiente.getIdUsuario();
    }

    public synchronized int tamano() {
        return cola.size();
    }

    public synchronized boolean contiene(String idUsuario) {
        for (ObservadorDisponibilidad o : cola) {
            if (o.getIdUsuario().equals(idUsuario)) {
                return true;
            }
        }
        return false;
    }

    /** Códigos de los usuarios en espera, en orden de llegada. */
    public synchronized List<String> idsEnEspera() {
        List<String> ids = new ArrayList<>();
        for (ObservadorDisponibilidad o : cola) {
            ids.add(o.getIdUsuario());
        }
        return ids;
    }
}
