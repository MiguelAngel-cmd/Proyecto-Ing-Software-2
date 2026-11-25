package com.biblioteca.espera;

import com.biblioteca.modelo.Recurso;

/** Patrón Observer: quien quiere enterarse de que un recurso quedó disponible. */
public interface ObservadorDisponibilidad {
    String getId();

    void recursoDisponible(Recurso recurso);
}
