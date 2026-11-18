package com.biblioteca.fabrica;

import com.biblioteca.modelo.TipoRecurso;
import java.util.EnumMap;
import java.util.Map;

/** Entrega la fábrica que corresponde a cada {@link TipoRecurso}. */
public final class FabricasDeRecurso {

    private static final Map<TipoRecurso, FabricaRecurso> FABRICAS = new EnumMap<>(TipoRecurso.class);

    static {
        FABRICAS.put(TipoRecurso.LIBRO, new FabricaLibro());
        FABRICAS.put(TipoRecurso.EQUIPO, new FabricaEquipo());
        FABRICAS.put(TipoRecurso.OTRO, new FabricaOtro());
    }

    private FabricasDeRecurso() {
    }

    public static FabricaRecurso para(TipoRecurso tipo) {
        return FABRICAS.get(tipo);
    }
}
