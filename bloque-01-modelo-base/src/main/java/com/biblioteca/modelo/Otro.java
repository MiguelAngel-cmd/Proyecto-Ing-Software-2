package com.biblioteca.modelo;

/** Regla (plazo en días, renovaciones, máximo por usuario) = (7, 1, 2). Dato propio: la categoría. */
public class Otro extends Recurso {
    private static final ReglaPrestamo REGLA = new ReglaPrestamo(7, 1, 2);

    public Otro(String id, String titulo, String detalle) {
        super(id, titulo, detalle);
    }

    @Override public TipoRecurso getTipo() { return TipoRecurso.OTRO; }
    @Override public ReglaPrestamo getRegla() { return REGLA; }
    @Override public String getEtiquetaDetalle() { return "Categoría"; }
}
