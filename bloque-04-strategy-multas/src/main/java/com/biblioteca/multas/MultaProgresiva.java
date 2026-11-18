package com.biblioteca.multas;

import com.biblioteca.modelo.Recurso;

/** Estrategia concreta: el día de retraso cuesta más entre más tarde se devuelva. */
public class MultaProgresiva implements PoliticaMulta {

    @Override
    public long calcular(long diasRetraso, Recurso recurso) {
        long total = 0;
        for (long dia = 1; dia <= diasRetraso; dia++) {
            if (dia <= 3) {
                total += 500;
            } else if (dia <= 10) {
                total += 1000;
            } else {
                total += 2000;
            }
        }
        return total;
    }

    @Override
    public String getNombre() {
        return "Progresiva";
    }

    @Override
    public String getDescripcion() {
        return "Días 1-3: $500 c/u, días 4-10: $1.000 c/u, desde el día 11: $2.000 c/u";
    }
}
