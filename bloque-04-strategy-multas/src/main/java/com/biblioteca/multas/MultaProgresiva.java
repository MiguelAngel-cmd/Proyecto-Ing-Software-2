package com.biblioteca.multas;

import com.biblioteca.modelo.Recurso;

public class MultaProgresiva implements PoliticaMulta {
    @Override
    public long calcular(long diasRetraso, Recurso recurso) {
        long total = 0;
        for (long dia = 1; dia <= diasRetraso; dia++) {
            total += dia <= 3 ? 500 : dia <= 10 ? 1000 : 2000;
        }
        return total;
    }

    @Override
    public String getNombre() { return "Progresiva: días 1-3 a $500, días 4-10 a $1.000, desde el 11 a $2.000"; }
}
