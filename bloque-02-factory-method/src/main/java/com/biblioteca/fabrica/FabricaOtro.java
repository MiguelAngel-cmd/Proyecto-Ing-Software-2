package com.biblioteca.fabrica;

import com.biblioteca.modelo.Otro;
import com.biblioteca.modelo.Recurso;

public class FabricaOtro extends FabricaRecurso {
    @Override
    protected Recurso crearRecurso(String id, String titulo, String detalle) {
        return new Otro(id, titulo, detalle);
    }
}
