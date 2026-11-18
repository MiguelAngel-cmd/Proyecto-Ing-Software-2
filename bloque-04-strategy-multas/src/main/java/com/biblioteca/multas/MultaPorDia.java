package com.biblioteca.multas;

import com.biblioteca.modelo.Recurso;

/** Estrategia concreta: un valor fijo por cada día de retraso. */
public class MultaPorDia implements PoliticaMulta {

    private static final long VALOR_DIA = 1000;

    @Override
    public long calcular(long diasRetraso, Recurso recurso) {
        return diasRetraso * VALOR_DIA;
    }

    @Override
    public String getNombre() {
        return "Por día";
    }

    @Override
    public String getDescripcion() {
        return "$1.000 por cada día de retraso";
    }
}
