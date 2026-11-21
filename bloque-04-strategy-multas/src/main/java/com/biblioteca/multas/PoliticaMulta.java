package com.biblioteca.multas;

import com.biblioteca.modelo.Recurso;
import java.util.List;

/** Patrón Strategy: contrato común de todas las fórmulas de multa (segunda área de variación). */
public interface PoliticaMulta {

    /** Valor de la multa para un préstamo con los días de retraso indicados (mayor que cero). */
    long calcular(long diasRetraso, Recurso recurso);

    /** Nombre y descripción que se muestran al usuario. */
    String getNombre();

    /** Políticas que se pueden activar. Agregar una nueva = una clase y una línea aquí. */
    List<PoliticaMulta> TODAS = List.of(new MultaPorDia(), new MultaProgresiva(), new MultaPorTipoRecurso());
}
