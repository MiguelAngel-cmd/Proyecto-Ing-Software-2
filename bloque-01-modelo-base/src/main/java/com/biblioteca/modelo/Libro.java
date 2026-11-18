package com.biblioteca.modelo;

import com.biblioteca.reglas.ReglaLibro;
import com.biblioteca.reglas.ReglaPrestamo;

/** Libro; su dato particular es el autor. */
public class Libro extends Recurso {

    private static final ReglaPrestamo REGLA = new ReglaLibro();

    public Libro(String id, String titulo, String autor) {
        super(id, titulo, autor);
    }

    @Override
    public TipoRecurso getTipo() {
        return TipoRecurso.LIBRO;
    }

    @Override
    public ReglaPrestamo getRegla() {
        return REGLA;
    }

    @Override
    public String getEtiquetaDetalle() {
        return "Autor";
    }
}
