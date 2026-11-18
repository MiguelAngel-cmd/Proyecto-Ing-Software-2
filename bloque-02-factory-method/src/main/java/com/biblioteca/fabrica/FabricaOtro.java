package com.biblioteca.fabrica;

import com.biblioteca.modelo.Otro;
import com.biblioteca.modelo.Recurso;
import com.biblioteca.modelo.TipoRecurso;

/** Fábrica concreta (ConcreteCreator): construye objetos {@link Otro}. */
public class FabricaOtro extends FabricaRecurso {

    @Override
    protected Recurso crearRecurso(DatosRecurso datos) {
        return new Otro(datos.getId(), datos.getTitulo(), datos.getDetalle());
    }

    @Override
    public TipoRecurso getTipo() {
        return TipoRecurso.OTRO;
    }
}
