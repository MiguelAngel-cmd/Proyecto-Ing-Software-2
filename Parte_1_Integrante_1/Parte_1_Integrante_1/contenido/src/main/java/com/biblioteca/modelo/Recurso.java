package com.biblioteca.modelo;

import com.biblioteca.excepcion.BibliotecaException;

/** Lo que la biblioteca presta. Es abstracta: cada tipo define su regla y el nombre de su dato propio. */
public abstract class Recurso {
    private final String id;
    private String titulo;
    private String detalle;
    private boolean prestado;
    private boolean deBaja;

    protected Recurso(String id, String titulo, String detalle) {
        this.id = id;
        this.titulo = titulo;
        this.detalle = detalle;
    }

    public abstract TipoRecurso getTipo();

    public abstract ReglaPrestamo getRegla();

    /** Nombre del dato propio del tipo: "Autor", "Marca" o "Categoría". */
    public abstract String getEtiquetaDetalle();

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDetalle() { return detalle; }
    public boolean estaDeBaja() { return deBaja; }
    public boolean estaDisponible() { return !prestado && !deBaja; }

    public String getEstado() {
        return deBaja ? "De baja" : prestado ? "Prestado" : "Disponible";
    }

    public void modificar(String nuevoTitulo, String nuevoDetalle) {
        if (deBaja) throw new BibliotecaException("No se puede modificar un recurso dado de baja.");
        this.titulo = nuevoTitulo;
        this.detalle = nuevoDetalle;
    }

    public void marcarPrestado() {
        if (!estaDisponible())
            throw new BibliotecaException("'" + titulo + "' no está disponible (" + getEstado() + ").");
        prestado = true;
    }

    public void marcarDisponible() { prestado = false; }

    public void darDeBaja() {
        if (prestado) throw new BibliotecaException("No se puede dar de baja '" + titulo + "': está prestado.");
        deBaja = true;
    }
}
