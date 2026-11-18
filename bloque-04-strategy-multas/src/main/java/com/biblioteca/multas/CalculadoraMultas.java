package com.biblioteca.multas;

import com.biblioteca.modelo.Recurso;

/**
 * Contexto del patrón Strategy: usa la política activa para calcular la multa
 * (RF-05) y permite cambiarla en tiempo de ejecución.
 */
public class CalculadoraMultas {

    private volatile PoliticaMulta politica;

    public CalculadoraMultas() {
        this(new MultaPorDia());
    }

    public CalculadoraMultas(PoliticaMulta politicaInicial) {
        this.politica = politicaInicial;
    }

    public void setPolitica(PoliticaMulta nueva) {
        this.politica = nueva;
    }

    public PoliticaMulta getPolitica() {
        return politica;
    }

    /** Multa por un retraso; si no hay retraso la multa es 0. */
    public long calcular(long diasRetraso, Recurso recurso) {
        if (diasRetraso <= 0) {
            return 0;
        }
        return politica.calcular(diasRetraso, recurso);
    }
}
