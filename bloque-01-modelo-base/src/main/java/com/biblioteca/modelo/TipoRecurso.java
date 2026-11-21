package com.biblioteca.modelo;

/** Tipos de recurso y la letra con la que empiezan sus códigos (L-001, E-001...). */
public enum TipoRecurso {
    LIBRO("Libro", "L"), EQUIPO("Equipo", "E"), OTRO("Otro", "O");

    private final String nombre;
    private final String prefijo;

    TipoRecurso(String nombre, String prefijo) {
        this.nombre = nombre;
        this.prefijo = prefijo;
    }

    public String getPrefijo() { return prefijo; }

    @Override
    public String toString() { return nombre; }
}
