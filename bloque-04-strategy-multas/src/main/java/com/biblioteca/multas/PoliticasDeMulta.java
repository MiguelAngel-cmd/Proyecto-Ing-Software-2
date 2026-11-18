package com.biblioteca.multas;

import java.util.List;

/** Lista de políticas que el usuario puede elegir desde la consola o la interfaz. */
public final class PoliticasDeMulta {

    private PoliticasDeMulta() {
    }

    public static List<PoliticaMulta> todas() {
        return List.of(new MultaPorDia(), new MultaProgresiva(), new MultaPorTipoRecurso());
    }
}
