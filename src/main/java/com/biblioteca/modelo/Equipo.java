package com.biblioteca.modelo;

/** Regla (plazo en días, renovaciones, máximo por usuario) = (3, 1, 1). Dato propio: la marca. */
public class Equipo extends Recurso {
    private static final ReglaPrestamo REGLA = new ReglaPrestamo(3, 1, 1);

    public Equipo(String id, String titulo, String detalle) {
        super(id, titulo, detalle);
    }

    @Override public TipoRecurso getTipo() { return TipoRecurso.EQUIPO; }
    @Override public ReglaPrestamo getRegla() { return REGLA; }
    @Override public String getEtiquetaDetalle() { return "Marca"; }
}
