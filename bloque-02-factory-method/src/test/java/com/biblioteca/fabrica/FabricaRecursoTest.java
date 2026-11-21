package com.biblioteca.fabrica;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import com.biblioteca.excepcion.BibliotecaException;
import com.biblioteca.modelo.Equipo;
import com.biblioteca.modelo.Libro;
import com.biblioteca.modelo.Otro;
import com.biblioteca.modelo.TipoRecurso;
import org.junit.Test;

/** Pruebas del patrón Factory Method. */
public class FabricaRecursoTest {

    @Test
    public void cadaFabricaCreaSuTipo() {
        assertTrue(FabricaRecurso.para(TipoRecurso.LIBRO).crear("L-001", "Clean Code", "Martin") instanceof Libro);
        assertTrue(FabricaRecurso.para(TipoRecurso.EQUIPO).crear("E-001", "Portátil", "Lenovo") instanceof Equipo);
        assertTrue(FabricaRecurso.para(TipoRecurso.OTRO).crear("O-001", "Sala", "Espacio") instanceof Otro);
    }

    @Test
    public void elTituloYElDetalleSonObligatorios() {
        FabricaRecurso fabrica = FabricaRecurso.para(TipoRecurso.LIBRO);
        assertThrows(BibliotecaException.class, () -> fabrica.crear("L-001", "  ", "Martin"));
        assertThrows(BibliotecaException.class, () -> fabrica.crear("L-001", "Clean Code", ""));
    }

    @Test
    public void quitaEspaciosSobrantes() {
        assertEquals("Clean Code", FabricaRecurso.para(TipoRecurso.LIBRO).crear("L-001", "  Clean Code ", "Martin").getTitulo());
    }
}
