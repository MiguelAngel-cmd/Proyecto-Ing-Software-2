package com.biblioteca.fabrica;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.biblioteca.excepcion.BibliotecaException;
import com.biblioteca.modelo.Equipo;
import com.biblioteca.modelo.Libro;
import com.biblioteca.modelo.Otro;
import com.biblioteca.modelo.Recurso;
import com.biblioteca.modelo.TipoRecurso;
import org.junit.Test;

public class FabricaRecursoTest {

    @Test
    public void cadaFabricaCreaElTipoCorrecto() {
        Recurso libro = FabricasDeRecurso.para(TipoRecurso.LIBRO)
                .crear(new DatosRecurso("L-001", "Clean Code", "Robert C. Martin"));
        Recurso equipo = FabricasDeRecurso.para(TipoRecurso.EQUIPO)
                .crear(new DatosRecurso("E-001", "Portátil", "Lenovo"));
        Recurso otro = FabricasDeRecurso.para(TipoRecurso.OTRO)
                .crear(new DatosRecurso("O-001", "Sala 1", "Espacio"));

        assertTrue(libro instanceof Libro);
        assertTrue(equipo instanceof Equipo);
        assertTrue(otro instanceof Otro);
    }

    @Test
    public void elRecursoCreadoConservaLosDatos() {
        Recurso r = new FabricaLibro().crear(new DatosRecurso("L-007", "Refactoring", "Martin Fowler"));
        assertEquals("L-007", r.getId());
        assertEquals("Refactoring", r.getTitulo());
        assertEquals("Martin Fowler", r.getDetalle());
    }

    @Test
    public void hayUnaFabricaParaCadaTipo() {
        for (TipoRecurso tipo : TipoRecurso.values()) {
            assertEquals(tipo, FabricasDeRecurso.para(tipo).getTipo());
        }
    }

    @Test
    public void noCreaUnRecursoSinTitulo() {
        try {
            new FabricaLibro().crear(new DatosRecurso("L-001", "  ", "Autor"));
            fail("Debió lanzar BibliotecaException");
        } catch (BibliotecaException e) {
            assertTrue(e.getMessage().contains("título"));
        }
    }

    @Test
    public void noCreaUnRecursoSinSuDatoParticular() {
        try {
            new FabricaEquipo().crear(new DatosRecurso("E-001", "Portátil", ""));
            fail("Debió lanzar BibliotecaException");
        } catch (BibliotecaException e) {
            assertTrue(e.getMessage().contains("Marca"));
        }
    }
}
