package com.biblioteca.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.biblioteca.excepcion.BibliotecaException;
import org.junit.Test;

public class RecursoTest {

    @Test
    public void cadaTipoTieneSuPropiaReglaDePrestamo() {
        Recurso libro = new Libro("L-001", "Clean Code", "Robert C. Martin");
        Recurso equipo = new Equipo("E-001", "Portátil", "Lenovo");
        Recurso otro = new Otro("O-001", "Sala de estudio", "Espacio");

        assertEquals(14, libro.getRegla().getPlazoDias());
        assertEquals(2, libro.getRegla().getMaxRenovaciones());
        assertEquals(3, libro.getRegla().getMaxPrestamosPorUsuario());

        assertEquals(3, equipo.getRegla().getPlazoDias());
        assertEquals(1, equipo.getRegla().getMaxRenovaciones());
        assertEquals(1, equipo.getRegla().getMaxPrestamosPorUsuario());

        assertEquals(7, otro.getRegla().getPlazoDias());
        assertEquals(1, otro.getRegla().getMaxRenovaciones());
        assertEquals(2, otro.getRegla().getMaxPrestamosPorUsuario());
    }

    @Test
    public void cadaTipoDiceQueEsYCualEsSuDatoParticular() {
        assertEquals(TipoRecurso.LIBRO, new Libro("L-001", "t", "a").getTipo());
        assertEquals("Autor", new Libro("L-001", "t", "a").getEtiquetaDetalle());
        assertEquals("Marca", new Equipo("E-001", "t", "m").getEtiquetaDetalle());
        assertEquals("Categoría", new Otro("O-001", "t", "c").getEtiquetaDetalle());
    }

    @Test
    public void unRecursoNuevoEstaDisponible() {
        assertTrue(new Libro("L-001", "t", "a").estaDisponible());
    }

    @Test
    public void prestarYDevolverCambiaElEstado() {
        Recurso r = new Libro("L-001", "t", "a");
        r.marcarPrestado();
        assertEquals(EstadoRecurso.PRESTADO, r.getEstado());
        assertFalse(r.estaDisponible());
        r.marcarDisponible();
        assertTrue(r.estaDisponible());
    }

    @Test
    public void noSePuedePrestarDosVeces() {
        Recurso r = new Libro("L-001", "t", "a");
        r.marcarPrestado();
        try {
            r.marcarPrestado();
            fail("Debió lanzar BibliotecaException");
        } catch (BibliotecaException e) {
            assertTrue(e.getMessage().contains("no está disponible"));
        }
    }

    @Test
    public void noSePuedeDarDeBajaUnRecursoPrestado() {
        Recurso r = new Equipo("E-001", "Portátil", "HP");
        r.marcarPrestado();
        try {
            r.darDeBaja();
            fail("Debió lanzar BibliotecaException");
        } catch (BibliotecaException e) {
            assertTrue(e.getMessage().contains("está prestado"));
        }
    }

    @Test
    public void unRecursoDeBajaYaNoSePuedePrestar() {
        Recurso r = new Libro("L-001", "t", "a");
        r.darDeBaja();
        assertEquals(EstadoRecurso.DE_BAJA, r.getEstado());
        try {
            r.marcarPrestado();
            fail("Debió lanzar BibliotecaException");
        } catch (BibliotecaException e) {
            assertTrue(e.getMessage().contains("De baja"));
        }
    }

    @Test
    public void modificarCambiaTituloYDetalle() {
        Recurso r = new Libro("L-001", "Viejo", "Autor A");
        r.modificar("Nuevo", "Autor B");
        assertEquals("Nuevo", r.getTitulo());
        assertEquals("Autor B", r.getDetalle());
    }
}
