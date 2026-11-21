package com.biblioteca.fabrica;

import com.biblioteca.modelo.Equipo;
import com.biblioteca.modelo.Recurso;

public class FabricaEquipo extends FabricaRecurso {
    @Override
    protected Recurso crearRecurso(String id, String titulo, String detalle) {
        return new Equipo(id, titulo, detalle);
    }
}
