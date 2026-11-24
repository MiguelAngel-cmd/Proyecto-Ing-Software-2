package com.biblioteca.multas;

import com.biblioteca.modelo.Recurso;

public class MultaPorDia implements PoliticaMulta {
    @Override
    public long calcular(long diasRetraso, Recurso recurso) {
        return diasRetraso * 1000;
    }

    @Override
    public String getNombre() { return "Por día: $1.000 por cada día de retraso"; }
}
