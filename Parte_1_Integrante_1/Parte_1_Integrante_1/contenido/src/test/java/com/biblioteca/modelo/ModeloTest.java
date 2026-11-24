package com.biblioteca.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.biblioteca.excepcion.BibliotecaException;
import org.junit.Test;

public class ModeloTest {

    @Test
    public void cadaTipoTraeSuPropiaRegla() {
        assertEquals(14, new Libro("L-001", "Clean Code", "Martin").getRegla().plazoDias());
        assertEquals(3, new Equipo("E-001", "Portátil", "Lenovo").getRegla().plazoDias());
        assertEquals(7, new Otro("O-001", "Sala", "Espacio").getRegla().plazoDias());
    }

    @Test
    public void unRecursoPrestadoNoEstaDisponible() {
        Recurso r = new Libro("L-001", "Clean Code", "Martin");
        assertTrue(r.estaDisponible());
        r.marcarPrestado();
        assertFalse(r.estaDisponible());
        assertThrows(BibliotecaException.class, r::marcarPrestado);
        r.marcarDisponible();
        assertTrue(r.estaDisponible());
    }

    @Test
    public void noSeDaDeBajaUnRecursoPrestado() {
        Recurso r = new Libro("L-001", "Clean Code", "Martin");
        r.marcarPrestado();
        assertThrows(BibliotecaException.class, r::darDeBaja);
    }
}
