package com.biblioteca.espera;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.biblioteca.excepcion.BibliotecaException;
import com.biblioteca.modelo.Libro;
import com.biblioteca.modelo.Recurso;
import com.biblioteca.modelo.Usuario;
import org.junit.Test;

/** Pruebas del patrón Observer: el usuario en la fila es el observador. */
public class ListaEsperaTest {
    private final Recurso libro = new Libro("L-001", "Clean Code", "Martin");
    private final Usuario ana = new Usuario("ana", "Ana");
    private final Usuario luis = new Usuario("luis", "Luis");

    @Test
    public void avisaSoloAlPrimeroDeLaFila() {
        ListaEspera lista = new ListaEspera(libro);
        lista.suscribir(ana);
        lista.suscribir(luis);

        assertEquals("ana", lista.notificarSiguiente());

        assertEquals(1, ana.getAvisos().size());
        assertEquals(0, luis.getAvisos().size());
        assertEquals(1, lista.tamano());
    }

    @Test
    public void conLaFilaVaciaNoAvisaANadie() {
        assertNull(new ListaEspera(libro).notificarSiguiente());
    }

    @Test
    public void unUsuarioNoPuedeEstarDosVeces() {
        ListaEspera lista = new ListaEspera(libro);
        lista.suscribir(ana);
        assertThrows(BibliotecaException.class, () -> lista.suscribir(ana));
    }

    @Test
    public void quienCancelaYaNoRecibeAviso() {
        ListaEspera lista = new ListaEspera(libro);
        lista.suscribir(ana);
        lista.cancelar("ana");
        assertNull(lista.notificarSiguiente());
    }
}
