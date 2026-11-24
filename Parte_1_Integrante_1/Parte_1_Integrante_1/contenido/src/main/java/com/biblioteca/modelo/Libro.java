package com.biblioteca.modelo;

/** Regla (plazo en días, renovaciones, máximo por usuario) = (14, 2, 3). Dato propio: el autor. */
public class Libro extends Recurso {
    private static final ReglaPrestamo REGLA = new ReglaPrestamo(14, 2, 3);

    public Libro(String id, String titulo, String detalle) {
        super(id, titulo, detalle);
    }

    @Override public TipoRecurso getTipo() { return TipoRecurso.LIBRO; }
    @Override public ReglaPrestamo getRegla() { return REGLA; }
    @Override public String getEtiquetaDetalle() { return "Autor"; }
}
