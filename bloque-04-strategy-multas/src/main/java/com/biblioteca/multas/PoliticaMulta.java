package com.biblioteca.multas;

import com.biblioteca.modelo.Recurso;

/**
 * PATRÓN STRATEGY (comportamiento) — rol: Strategy (RNF-05).
 * Cada política de multa es una forma distinta de calcular el valor a pagar.
 * Se pueden cambiar en cualquier momento sin modificar el código que presta o devuelve.
 */
public interface PoliticaMulta {

    /** Valor de la multa en pesos para un retraso de {@code diasRetraso} días (> 0). */
    long calcular(long diasRetraso, Recurso recurso);

    String getNombre();

    String getDescripcion();
}
