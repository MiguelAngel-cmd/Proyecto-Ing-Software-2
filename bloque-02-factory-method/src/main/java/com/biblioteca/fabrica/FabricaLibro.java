package com.biblioteca.fabrica;

import com.biblioteca.modelo.Libro;
import com.biblioteca.modelo.Recurso;
import com.biblioteca.modelo.TipoRecurso;

/** Fábrica concreta (ConcreteCreator): construye objetos {@link Libro}. */
public class FabricaLibro extends FabricaRecurso {

    @Override
    protected Recurso crearRecurso(DatosRecurso datos) {
        return new Libro(datos.getId(), datos.getTitulo(), datos.getDetalle());
    }

    @Override
    public TipoRecurso getTipo() {
        return TipoRecurso.LIBRO;
    }
}
