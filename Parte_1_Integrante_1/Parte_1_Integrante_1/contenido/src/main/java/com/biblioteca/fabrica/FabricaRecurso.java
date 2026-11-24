package com.biblioteca.fabrica;

import com.biblioteca.excepcion.BibliotecaException;
import com.biblioteca.modelo.Recurso;
import com.biblioteca.modelo.TipoRecurso;

/**
 * Patrón Factory Method. {@link #crear} es el método plantilla (valida lo común);
 * {@link #crearRecurso} es el método fábrica que cada tipo implementa.
 */
public abstract class FabricaRecurso {

    public final Recurso crear(String id, String titulo, String detalle) {
        if (titulo == null || titulo.isBlank()) throw new BibliotecaException("El título es obligatorio.");
        if (detalle == null || detalle.isBlank())
            throw new BibliotecaException("El autor, marca o categoría es obligatorio.");
        return crearRecurso(id, titulo.trim(), detalle.trim());
    }

    protected abstract Recurso crearRecurso(String id, String titulo, String detalle);

    /** Único lugar que relaciona cada tipo con su fábrica. */
    public static FabricaRecurso para(TipoRecurso tipo) {
        return switch (tipo) {
            case LIBRO -> new FabricaLibro();
            case EQUIPO -> new FabricaEquipo();
            case OTRO -> new FabricaOtro();
        };
    }
}
