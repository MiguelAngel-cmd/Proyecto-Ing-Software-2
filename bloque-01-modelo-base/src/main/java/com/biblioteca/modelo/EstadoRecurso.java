package com.biblioteca.modelo;

/** Estados posibles de un recurso dentro del catálogo. */
public enum EstadoRecurso {
    DISPONIBLE("Disponible"),
    PRESTADO("Prestado"),
    DE_BAJA("De baja");

    private final String nombre;

    EstadoRecurso(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
