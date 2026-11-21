package com.biblioteca.fabrica;

import com.biblioteca.modelo.Libro;
import com.biblioteca.modelo.Recurso;

public class FabricaLibro extends FabricaRecurso {
    @Override
    protected Recurso crearRecurso(String id, String titulo, String detalle) {
        return new Libro(id, titulo, detalle);
    }
}
