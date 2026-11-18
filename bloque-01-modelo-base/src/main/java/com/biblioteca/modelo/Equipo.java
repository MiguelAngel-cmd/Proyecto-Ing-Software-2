package com.biblioteca.modelo;

import com.biblioteca.reglas.ReglaEquipo;
import com.biblioteca.reglas.ReglaPrestamo;

/** Equipo (portátil, proyector, cámara...); su dato particular es la marca. */
public class Equipo extends Recurso {

    private static final ReglaPrestamo REGLA = new ReglaEquipo();

    public Equipo(String id, String titulo, String marca) {
        super(id, titulo, marca);
    }

    @Override
    public TipoRecurso getTipo() {
        return TipoRecurso.EQUIPO;
    }

    @Override
    public ReglaPrestamo getRegla() {
        return REGLA;
    }

    @Override
    public String getEtiquetaDetalle() {
        return "Marca";
    }
}
