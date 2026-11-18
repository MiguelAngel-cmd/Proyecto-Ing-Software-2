package com.biblioteca.espera;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.biblioteca.excepcion.BibliotecaException;
import com.biblioteca.modelo.Libro;
import com.biblioteca.modelo.Recurso;
import com.biblioteca.modelo.Usuario;
import org.junit.Before;
import org.junit.Test;

public class ListaEsperaTest {

    private Recurso libro;
    private Usuario ana;
    private Usuario luis;
    private ListaEspera lista;

    @Before
    public void preparar() {
        libro = new Libro("L-001", "Clean Code", "Martin");
        ana = new Usuario("ana", "Ana");
        luis = new Usuario("luis", "Luis");
        lista = new ListaEspera(libro);
    }

    @Test
    public void avisaSoloAlPrimeroDeLaFila() {
        lista.suscribir(new AvisoUsuario(ana));
        lista.suscribir(new AvisoUsuario(luis));

        assertEquals("ana", lista.notificarSiguiente());

        assertEquals(1, ana.getAvisos().size());
        assertTrue(ana.getAvisos().get(0).contains("Clean Code"));
        assertEquals(0, luis.getAvisos().size());
        assertEquals(1, lista.tamano());
    }

    @Test
    public void respetaElOrdenDeLlegada() {
        lista.suscribir(new AvisoUsuario(luis));
        lista.suscribir(new AvisoUsuario(ana));
        assertEquals("luis", lista.notificarSiguiente());
        assertEquals("ana", lista.notificarSiguiente());
        assertNull(lista.notificarSiguiente());
    }

    @Test
    public void noPermiteEntrarDosVeces() {
        lista.suscribir(new AvisoUsuario(ana));
        try {
            lista.suscribir(new AvisoUsuario(ana));
            fail("Debió lanzar BibliotecaException");
        } catch (BibliotecaException e) {
            assertTrue(e.getMessage().contains("Ya está en la lista"));
        }
    }

    @Test
    public void cancelarSacaAlUsuarioDeLaFila() {
        lista.suscribir(new AvisoUsuario(ana));
        lista.suscribir(new AvisoUsuario(luis));
        lista.cancelar("ana");
        assertEquals("luis", lista.notificarSiguiente());
    }

    @Test
    public void sinNadieEnEsperaNoAvisaANadie() {
        assertNull(lista.notificarSiguiente());
    }

    @Test
    public void elGestorTieneUnaListaPorRecurso() {
        GestorListasEspera gestor = new GestorListasEspera();
        Recurso otro = new Libro("L-002", "Refactoring", "Fowler");
        gestor.unirse(libro, new AvisoUsuario(ana));
        gestor.unirse(otro, new AvisoUsuario(luis));

        assertEquals(1, gestor.cantidadEnEspera(libro));
        assertEquals("ana", gestor.notificarDisponible(libro));
        assertEquals(0, gestor.cantidadEnEspera(libro));
        assertEquals(1, gestor.cantidadEnEspera(otro));
    }
}
