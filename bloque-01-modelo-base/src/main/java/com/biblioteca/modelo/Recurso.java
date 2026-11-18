package com.biblioteca.modelo;

import com.biblioteca.excepcion.BibliotecaException;
import com.biblioteca.reglas.ReglaPrestamo;

/**
 * Cualquier cosa que la biblioteca presta. Es abstracta: los tipos concretos
 * (Libro, Equipo, Otro) definen su tipo, su regla de préstamo y el nombre de su
 * dato particular (autor, marca, categoría).
 */
public abstract class Recurso {

    private final String id;
    private String titulo;
    private String detalle;
    private EstadoRecurso estado = EstadoRecurso.DISPONIBLE;

    protected Recurso(String id, String titulo, String detalle) {
        this.id = id;
        this.titulo = titulo;
        this.detalle = detalle;
    }

    public abstract TipoRecurso getTipo();

    /** Regla de préstamo propia del tipo de recurso. */
    public abstract ReglaPrestamo getRegla();

    /** Nombre del dato particular del tipo: "Autor", "Marca", "Categoría". */
    public abstract String getEtiquetaDetalle();

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDetalle() {
        return detalle;
    }

    public EstadoRecurso getEstado() {
        return estado;
    }

    public synchronized void modificar(String nuevoTitulo, String nuevoDetalle) {
        if (estado == EstadoRecurso.DE_BAJA) {
            throw new BibliotecaException("No se puede modificar un recurso dado de baja.");
        }
        this.titulo = nuevoTitulo;
        this.detalle = nuevoDetalle;
    }

    public synchronized boolean estaDisponible() {
        return estado == EstadoRecurso.DISPONIBLE;
    }

    public synchronized void marcarPrestado() {
        if (estado != EstadoRecurso.DISPONIBLE) {
            throw new BibliotecaException("El recurso '" + titulo + "' no está disponible (" + estado + ").");
        }
        estado = EstadoRecurso.PRESTADO;
    }

    public synchronized void marcarDisponible() {
        if (estado == EstadoRecurso.DE_BAJA) {
            throw new BibliotecaException("El recurso '" + titulo + "' está dado de baja.");
        }
        estado = EstadoRecurso.DISPONIBLE;
    }

    public synchronized void darDeBaja() {
        if (estado == EstadoRecurso.PRESTADO) {
            throw new BibliotecaException("No se puede dar de baja '" + titulo + "': está prestado.");
        }
        estado = EstadoRecurso.DE_BAJA;
    }
}
