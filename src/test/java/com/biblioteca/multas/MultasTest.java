package com.biblioteca.multas;

import static org.junit.Assert.assertEquals;

import com.biblioteca.modelo.Equipo;
import com.biblioteca.modelo.Libro;
import com.biblioteca.modelo.Recurso;
import org.junit.Test;

/** Pruebas del patrón Strategy: cada política calcula distinto con la misma interfaz. */
public class MultasTest {
    private final Recurso libro = new Libro("L-001", "Clean Code", "Martin");
    private final Recurso equipo = new Equipo("E-001", "Portátil", "Lenovo");

    @Test
    public void porDiaCobraMilPesosPorDia() {
        assertEquals(5000, new MultaPorDia().calcular(5, libro));
    }

    @Test
    public void progresivaEncarecePorTramos() {
        PoliticaMulta p = new MultaProgresiva();
        assertEquals(1500, p.calcular(3, libro));                    // 3 x 500
        assertEquals(1500 + 7 * 1000, p.calcular(10, libro));       // + 7 x 1.000
        assertEquals(1500 + 7000 + 2000, p.calcular(11, libro));    // + 1 x 2.000
    }

    @Test
    public void porTipoDependeDelRecurso() {
        PoliticaMulta p = new MultaPorTipoRecurso();
        assertEquals(1000, p.calcular(2, libro));       // 2 x 500
        assertEquals(6000, p.calcular(2, equipo));      // 2 x 3.000
    }

    @Test
    public void todasLasPoliticasSePuedenUsarPorLaInterfaz() {
        for (PoliticaMulta p : PoliticaMulta.TODAS) assertEquals(true, p.calcular(1, libro) > 0);
    }
}
