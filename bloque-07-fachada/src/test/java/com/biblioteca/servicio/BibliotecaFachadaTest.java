package com.biblioteca.servicio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.biblioteca.catalogo.Catalogo;
import com.biblioteca.excepcion.BibliotecaException;
import com.biblioteca.modelo.Prestamo;
import com.biblioteca.modelo.TipoRecurso;
import com.biblioteca.modelo.Usuario;
import com.biblioteca.util.Reloj;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

/** Prueba el sistema completo a través de la fachada (Facade), con los demás patrones trabajando juntos. */
public class BibliotecaFachadaTest {
    private BibliotecaFachada fachada;

    @Before
    public void preparar() {
        Catalogo.getInstancia().vaciar();
        fachada = new BibliotecaFachada(new Reloj(LocalDate.of(2026, 11, 1)));
        fachada.registrarUsuario(new Usuario("ana", "Ana"));
        fachada.registrarUsuario(new Usuario("luis", "Luis"));
        fachada.registrarRecurso(TipoRecurso.LIBRO, "Clean Code", "Martin");          // L-001
        fachada.registrarRecurso(TipoRecurso.LIBRO, "Refactoring", "Fowler");         // L-002
        fachada.registrarRecurso(TipoRecurso.EQUIPO, "Portátil", "Lenovo");           // E-001
        fachada.registrarRecurso(TipoRecurso.EQUIPO, "Proyector", "Epson");           // E-002
    }

    @Test
    public void elVencimientoDependeDelTipoDeRecurso() {
        assertEquals(LocalDate.of(2026, 11, 15), fachada.prestar("L-001", "ana").getFechaVencimiento());   // libro: 14 días
        assertEquals(LocalDate.of(2026, 11, 4), fachada.prestar("E-001", "ana").getFechaVencimiento());    // equipo: 3 días
    }

    @Test
    public void unRecursoPrestadoNoSePuedePrestarDeNuevo() {
        fachada.prestar("L-001", "ana");
        assertThrows(BibliotecaException.class, () -> fachada.prestar("L-001", "luis"));
    }

    @Test
    public void elLimiteDePrestamosDependeDelTipo() {
        fachada.prestar("E-001", "ana");                                              // equipo: máximo 1 por usuario
        assertThrows(BibliotecaException.class, () -> fachada.prestar("E-002", "ana"));
        fachada.prestar("L-001", "ana");                                              // libros: otro límite
    }

    @Test
    public void laRenovacionRespetaElMaximoDelTipo() {
        int id = fachada.prestar("E-001", "ana").getId();                             // equipo: 1 renovación
        fachada.renovar(id);
        assertThrows(BibliotecaException.class, () -> fachada.renovar(id));
    }

    @Test
    public void devolverATiempoNoCobraMulta() {
        int id = fachada.prestar("L-001", "ana").getId();
        assertEquals(0, fachada.devolver(id).getMulta());
        assertTrue(fachada.listarRecursos().get(0).estaDisponible());
    }

    @Test
    public void devolverTardeCobraSegunLaPoliticaVigente() {
        int id = fachada.prestar("L-001", "ana").getId();                             // vence el 15 de noviembre
        fachada.avanzarDias(19);                                                      // hoy es 20: 5 días de retraso
        assertEquals(5000, fachada.devolver(id).getMulta());                          // política por día
    }

    @Test
    public void cambiarLaPoliticaCambiaElResultado() {
        int id = fachada.prestar("L-001", "ana").getId();
        fachada.avanzarDias(19);
        fachada.cambiarPolitica(2);                                                   // por tipo: libro $500/día
        assertEquals(2500, fachada.devolver(id).getMulta());
    }

    @Test
    public void alDevolverSeAvisaAlPrimeroDeLaListaDeEspera() {
        Prestamo p = fachada.prestar("L-001", "ana");
        fachada.unirseListaEspera("L-001", "luis");
        assertEquals(1, fachada.cantidadEnEspera("L-001"));

        fachada.devolver(p.getId());

        assertEquals(1, fachada.avisos("luis").size());
        assertEquals(0, fachada.cantidadEnEspera("L-001"));
    }

    @Test
    public void noSeRenuevaSiHayAlguienEsperando() {
        int id = fachada.prestar("L-001", "ana").getId();
        fachada.unirseListaEspera("L-001", "luis");
        assertThrows(BibliotecaException.class, () -> fachada.renovar(id));
    }

    @Test
    public void noHaceFaltaEsperarUnRecursoDisponible() {
        assertThrows(BibliotecaException.class, () -> fachada.unirseListaEspera("L-001", "luis"));
    }

    @Test
    public void unUsuarioInexistenteLanzaError() {
        assertThrows(BibliotecaException.class, () -> fachada.prestar("L-001", "pepe"));
    }
}
