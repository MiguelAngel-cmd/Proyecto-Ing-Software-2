package com.biblioteca.servicio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.biblioteca.catalogo.Catalogo;
import com.biblioteca.dto.PrestamoDTO;
import com.biblioteca.excepcion.BibliotecaException;
import com.biblioteca.modelo.TipoRecurso;
import com.biblioteca.modelo.Usuario;
import com.biblioteca.util.Dinero;
import com.biblioteca.util.Reloj;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

public class BibliotecaFachadaTest {

    private BibliotecaFachada fachada;

    @Before
    public void preparar() {
        Catalogo.getInstancia().vaciar();
        fachada = new BibliotecaFachada(new Reloj(LocalDate.of(2026, 11, 1)));
        fachada.registrarUsuario(new Usuario("ana", "Ana"));
        fachada.registrarUsuario(new Usuario("luis", "Luis"));
        fachada.registrarRecurso(TipoRecurso.LIBRO, "Clean Code", "Martin");   // L-001
        fachada.registrarRecurso(TipoRecurso.EQUIPO, "Portátil", "HP");        // E-001
    }

    private void esperaError(Runnable accion, String fragmento) {
        try {
            accion.run();
            fail("Debió lanzar BibliotecaException con: " + fragmento);
        } catch (BibliotecaException e) {
            assertTrue("Mensaje real: " + e.getMessage(), e.getMessage().contains(fragmento));
        }
    }

    @Test
    public void listaYBuscaRecursos() {
        assertEquals(2, fachada.listarRecursos().size());
        assertEquals(1, fachada.buscarRecursos("clean").size());
    }

    @Test
    public void flujoCompletoPrestarDevolverConMulta() {
        PrestamoDTO p = fachada.prestar("L-001", "ana");
        assertEquals(LocalDate.of(2026, 11, 15), p.fechaVencimiento());
        assertEquals("Prestado", fachada.buscarRecursos("clean").get(0).estado());

        fachada.avanzarDias(14 + 3);
        PrestamoDTO vencido = fachada.prestamosActivos().get(0);
        assertEquals(3, vencido.diasRetraso());
        assertEquals(3000, vencido.multa());                         // multa estimada

        PrestamoDTO devuelto = fachada.devolver(p.id());
        assertFalse(devuelto.activo());
        assertEquals(3000, devuelto.multa());
        assertEquals("Disponible", fachada.buscarRecursos("clean").get(0).estado());
    }

    @Test
    public void cambiarLaPoliticaCambiaElValorDeLaMulta() {
        PrestamoDTO p = fachada.prestar("L-001", "ana");
        fachada.cambiarPolitica(1);                                  // progresiva
        assertEquals("Progresiva", fachada.politicaActual().getNombre());
        fachada.avanzarDias(14 + 5);
        assertEquals(3500, fachada.devolver(p.id()).multa());
    }

    @Test
    public void renovarYListaDeEsperaConAviso() {
        PrestamoDTO p = fachada.prestar("L-001", "ana");
        assertEquals(1, fachada.renovar(p.id()).renovaciones());

        fachada.unirseListaEspera("L-001", "luis");
        assertEquals(1, fachada.cantidadEnEspera("L-001"));

        fachada.devolver(p.id());
        assertEquals(1, fachada.avisos("luis").size());
        fachada.limpiarAvisos("luis");
        assertEquals(0, fachada.avisos("luis").size());
    }

    @Test
    public void prestarAUnUsuarioInexistenteLanzaError() {
        esperaError(() -> fachada.prestar("L-001", "nadie"), "No existe el usuario");
    }

    @Test
    public void historialIncluyeLosPrestamosDevueltos() {
        PrestamoDTO p = fachada.prestar("L-001", "ana");
        fachada.devolver(p.id());
        assertEquals(0, fachada.prestamosActivos().size());
        assertEquals(1, fachada.historial().size());
    }

    @Test
    public void formatoDePesos() {
        assertEquals("$0", Dinero.formato(0));
        assertEquals("$12.500", Dinero.formato(12500));
        assertEquals("$1.234.567", Dinero.formato(1234567));
    }
}
