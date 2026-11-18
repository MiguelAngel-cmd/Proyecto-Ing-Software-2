package com.biblioteca.modelo;

/** Tipos de recurso que maneja la biblioteca. */
public enum TipoRecurso {
    LIBRO("Libro", "L"),
    EQUIPO("Equipo", "E"),
    OTRO("Otro", "O");

    private final String nombre;
    private final String prefijoId;

    TipoRecurso(String nombre, String prefijoId) {
        this.nombre = nombre;
        this.prefijoId = prefijoId;
    }

    public String getNombre() {
        return nombre;
    }

    /** Letra con la que empiezan los códigos de este tipo (L-001, E-001...). */
    public String getPrefijoId() {
        return prefijoId;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
