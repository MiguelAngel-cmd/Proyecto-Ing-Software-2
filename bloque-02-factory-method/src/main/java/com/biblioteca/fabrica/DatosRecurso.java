package com.biblioteca.fabrica;

/** Datos necesarios para crear un recurso. Es inmutable. */
public class DatosRecurso {

    private final String id;
    private final String titulo;
    private final String detalle;

    public DatosRecurso(String id, String titulo, String detalle) {
        this.id = id;
        this.titulo = titulo;
        this.detalle = detalle;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDetalle() {
        return detalle;
    }
}
