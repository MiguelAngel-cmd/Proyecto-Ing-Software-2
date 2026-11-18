package com.biblioteca.modelo;

import com.biblioteca.reglas.ReglaOtro;
import com.biblioteca.reglas.ReglaPrestamo;

/** Cualquier otro recurso (sala, juego de mesa, calculadora...); su dato es la categoría. */
public class Otro extends Recurso {

    private static final ReglaPrestamo REGLA = new ReglaOtro();

    public Otro(String id, String titulo, String categoria) {
        super(id, titulo, categoria);
    }

    @Override
    public TipoRecurso getTipo() {
        return TipoRecurso.OTRO;
    }

    @Override
    public ReglaPrestamo getRegla() {
        return REGLA;
    }

    @Override
    public String getEtiquetaDetalle() {
        return "Categoría";
    }
}
