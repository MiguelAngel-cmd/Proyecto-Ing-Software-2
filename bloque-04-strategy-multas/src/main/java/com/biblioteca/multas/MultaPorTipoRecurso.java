package com.biblioteca.multas;

import com.biblioteca.modelo.Recurso;

public class MultaPorTipoRecurso implements PoliticaMulta {
    @Override
    public long calcular(long diasRetraso, Recurso recurso) {
        long valorDia = switch (recurso.getTipo()) {
            case LIBRO -> 500;
            case EQUIPO -> 3000;
            case OTRO -> 1000;
        };
        return diasRetraso * valorDia;
    }

    @Override
    public String getNombre() { return "Por tipo: libro $500/día, equipo $3.000/día, otro $1.000/día"; }
}
