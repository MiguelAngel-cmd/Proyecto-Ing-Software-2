package com.biblioteca.catalogo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.biblioteca.dto.RecursoDTO;
import com.biblioteca.excepcion.BibliotecaException;
import com.biblioteca.modelo.EstadoRecurso;
import com.biblioteca.modelo.Recurso;
import com.biblioteca.modelo.TipoRecurso;
import org.junit.Before;
import org.junit.Test;

public class CatalogoTest {

    private Catalogo catalogo;

    @Before
    public void preparar() {
        catalogo = Catalogo.getInstancia();
        catalogo.vaciar();
    }

    @Test
    public void siempreDevuelveLaMismaInstancia() {
        assertSame(Catalogo.getInstancia(), Catalogo.getInstancia());
    }

    @Test
    public void generaCodigosConsecutivosPorTipo() {
        assertEquals("L-001", catalogo.registrar(TipoRecurso.LIBRO, "Clean Code", "Martin").getId());
        assertEquals("L-002", catalogo.registrar(TipoRecurso.LIBRO, "Refactoring", "Fowler").getId());
        assertEquals("E-001", catalogo.registrar(TipoRecurso.EQUIPO, "Portátil", "HP").getId());
    }

    @Test
    public void obtenerEncuentraPorCodigoSinImportarMayusculas() {
        catalogo.registrar(TipoRecurso.LIBRO, "Clean Code", "Martin");
        assertNotNull(catalogo.obtener("l-001"));
    }

    @Test
    public void obtenerUnCodigoInexistenteLanzaError() {
        try {
            catalogo.obtener("L-999");
            fail("Debió lanzar BibliotecaException");
        } catch (BibliotecaException e) {
            assertTrue(e.getMessage().contains("L-999"));
        }
    }

    @Test
    public void buscarEncuentraPorTituloOPorAutor() {
        catalogo.registrar(TipoRecurso.LIBRO, "Clean Code", "Robert Martin");
        catalogo.registrar(TipoRecurso.LIBRO, "Patrones de diseño", "Gamma");
        assertEquals(1, catalogo.buscar("clean").size());
        assertEquals(1, catalogo.buscar("gamma").size());
        assertEquals(2, catalogo.buscar("").size());
    }

    @Test
    public void modificarActualizaTituloYDetalle() {
        catalogo.registrar(TipoRecurso.LIBRO, "Viejo", "Autor A");
        Recurso r = catalogo.modificar("L-001", "Nuevo", "Autor B");
        assertEquals("Nuevo", r.getTitulo());
        assertEquals("Autor B", catalogo.obtener("L-001").getDetalle());
    }

    @Test
    public void darDeBajaLoQuitaDelListado() {
        catalogo.registrar(TipoRecurso.LIBRO, "Clean Code", "Martin");
        catalogo.registrar(TipoRecurso.LIBRO, "Refactoring", "Fowler");
        catalogo.darDeBaja("L-001");
        assertEquals(1, catalogo.listar().size());
        assertEquals(EstadoRecurso.DE_BAJA, catalogo.obtener("L-001").getEstado());
    }

    @Test
    public void elDtoCopiaLosDatosDelRecurso() {
        catalogo.registrar(TipoRecurso.EQUIPO, "Portátil", "HP");
        RecursoDTO dto = RecursoDTO.de(catalogo.obtener("E-001"));
        assertEquals("Equipo", dto.tipo());
        assertEquals("Marca", dto.etiquetaDetalle());
        assertEquals("Disponible", dto.estado());
        assertEquals(3, dto.plazoDias());
    }
}
