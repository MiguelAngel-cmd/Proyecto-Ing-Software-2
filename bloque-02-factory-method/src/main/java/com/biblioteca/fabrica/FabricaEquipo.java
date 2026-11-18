package com.biblioteca.fabrica;

import com.biblioteca.modelo.Equipo;
import com.biblioteca.modelo.Recurso;
import com.biblioteca.modelo.TipoRecurso;

/** Fábrica concreta (ConcreteCreator): construye objetos {@link Equipo}. */
public class FabricaEquipo extends FabricaRecurso {

    @Override
    protected Recurso crearRecurso(DatosRecurso datos) {
        return new Equipo(datos.getId(), datos.getTitulo(), datos.getDetalle());
    }

    @Override
    public TipoRecurso getTipo() {
        return TipoRecurso.EQUIPO;
    }
}
