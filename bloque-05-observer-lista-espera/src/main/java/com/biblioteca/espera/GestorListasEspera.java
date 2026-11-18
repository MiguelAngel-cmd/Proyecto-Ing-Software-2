package com.biblioteca.espera;

import com.biblioteca.modelo.Recurso;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Administra una {@link ListaEspera} por cada recurso, creándola cuando hace falta. */
public class GestorListasEspera {

    private final Map<String, ListaEspera> listas = new ConcurrentHashMap<>();

    private ListaEspera listaDe(Recurso recurso) {
        return listas.computeIfAbsent(recurso.getId(), id -> new ListaEspera(recurso));
    }

    public void unirse(Recurso recurso, ObservadorDisponibilidad observador) {
        listaDe(recurso).suscribir(observador);
    }

    public void salir(Recurso recurso, String idUsuario) {
        listaDe(recurso).cancelar(idUsuario);
    }

    /** RF-06: avisa al siguiente usuario de la lista. Devuelve su código o null. */
    public String notificarDisponible(Recurso recurso) {
        return listaDe(recurso).notificarSiguiente();
    }

    public int cantidadEnEspera(Recurso recurso) {
        return listaDe(recurso).tamano();
    }

    public List<String> idsEnEspera(Recurso recurso) {
        return listaDe(recurso).idsEnEspera();
    }
}
