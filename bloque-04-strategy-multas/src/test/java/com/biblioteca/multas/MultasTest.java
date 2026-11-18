package com.biblioteca.multas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import com.biblioteca.modelo.Equipo;
import com.biblioteca.modelo.Libro;
import com.biblioteca.modelo.Recurso;
import org.junit.Test;

public class MultasTest {

    private final Recurso libro = new Libro("L-001", "Clean Code", "Martin");
    private final Recurso equipo = new Equipo("E-001", "Portátil", "HP");

    @Test
    public void multaPorDiaCobraUnValorFijoPorDia() {
        assertEquals(3000, new MultaPorDia().calcular(3, libro));
    }

    @Test
    public void multaProgresivaCobraMasEntreMasTarde() {
        PoliticaMulta p = new MultaProgresiva();
        assertEquals(1000, p.calcular(2, libro));          // 2 x 500
        assertEquals(3500, p.calcular(5, libro));          // 3 x 500 + 2 x 1000
        assertEquals(12500, p.calcular(12, libro));        // 3 x 500 + 7 x 1000 + 2 x 2000
    }

    @Test
    public void multaPorTipoDependeDelRecurso() {
        PoliticaMulta p = new MultaPorTipoRecurso();
        assertEquals(2000, p.calcular(4, libro));          // 4 x 500
        assertEquals(6000, p.calcular(2, equipo));         // 2 x 3000
    }

    @Test
    public void sinRetrasoNoHayMulta() {
        CalculadoraMultas calc = new CalculadoraMultas();
        assertEquals(0, calc.calcular(0, libro));
        assertEquals(0, calc.calcular(-2, libro));
    }

    @Test
    public void laCalculadoraUsaLaPoliticaActivaYSePuedeCambiar() {
        CalculadoraMultas calc = new CalculadoraMultas();   // por defecto: por día
        assertEquals(5000, calc.calcular(5, libro));

        calc.setPolitica(new MultaProgresiva());            // se cambia sin tocar la calculadora
        assertEquals(3500, calc.calcular(5, libro));
    }

    @Test
    public void laCalculadoraIniciaConLaPoliticaPorDia() {
        assertEquals("Por día", new CalculadoraMultas().getPolitica().getNombre());
    }

    @Test
    public void laListaDePoliticasTieneTresOpciones() {
        assertEquals(3, PoliticasDeMulta.todas().size());
    }

    @Test
    public void sePuedeInyectarUnaPoliticaPropia() {
        PoliticaMulta gratis = new PoliticaMulta() {
            public long calcular(long dias, Recurso r) { return 0; }
            public String getNombre() { return "Sin multa"; }
            public String getDescripcion() { return "Perdón de multas"; }
        };
        CalculadoraMultas calc = new CalculadoraMultas(gratis);
        assertEquals(0, calc.calcular(10, libro));
        assertSame(gratis, calc.getPolitica());
    }
}
