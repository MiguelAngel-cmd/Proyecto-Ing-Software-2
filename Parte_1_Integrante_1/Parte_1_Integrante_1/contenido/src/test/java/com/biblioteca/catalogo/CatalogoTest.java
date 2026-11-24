package com.biblioteca.catalogo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import com.biblioteca.excepcion.BibliotecaException;
import com.biblioteca.modelo.TipoRecurso;
import org.junit.Before;
import org.junit.Test;

/** Pruebas del patrón Singleton (y del DAO que usa por dentro). */
public class CatalogoTest {

    @Before
    public void limpiar() {
        Catalogo.getInstancia().vaciar();
    }

    @Test
    public void siempreEsLaMismaInstancia() {
        assertSame(Catalogo.getInstancia(), Catalogo.getInstancia());
    }

    @Test
    public void loRegistradoSeVeDesdeCualquierLugar() {
        Catalogo.getInstancia().registrar(TipoRecurso.LIBRO, "Clean Code", "Martin");
        assertEquals("Clean Code", Catalogo.getInstancia().obtener("L-001").getTitulo());
    }

    @Test
    public void generaCodigosConsecutivosPorTipo() {
        Catalogo c = Catalogo.getInstancia();
        assertEquals("L-001", c.registrar(TipoRecurso.LIBRO, "A", "x").getId());
        assertEquals("L-002", c.registrar(TipoRecurso.LIBRO, "B", "x").getId());
        assertEquals("E-001", c.registrar(TipoRecurso.EQUIPO, "C", "x").getId());
    }

    @Test
    public void unRecursoDadoDeBajaDejaDeListarse() {
        Catalogo c = Catalogo.getInstancia();
        c.registrar(TipoRecurso.LIBRO, "A", "x");
        c.darDeBaja("L-001");
        assertEquals(0, c.listar().size());
    }

    @Test
    public void unCodigoInexistenteLanzaError() {
        assertThrows(BibliotecaException.class, () -> Catalogo.getInstancia().obtener("Z-999"));
    }
}
