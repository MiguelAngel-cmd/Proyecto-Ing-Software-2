package com.biblioteca.servicio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.biblioteca.catalogo.Catalogo;
import com.biblioteca.dao.memoria.PrestamoDAOMemoria;
import com.biblioteca.espera.GestorListasEspera;
import com.biblioteca.excepcion.BibliotecaException;
import com.biblioteca.modelo.Prestamo;
import com.biblioteca.modelo.TipoRecurso;
import com.biblioteca.modelo.Usuario;
import com.biblioteca.multas.CalculadoraMultas;
import com.biblioteca.multas.MultaProgresiva;
import com.biblioteca.util.Reloj;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

public class PrestamoServicioTest {

    private Catalogo catalogo;
    private Reloj reloj;
    private CalculadoraMultas multas;
    private PrestamoServicio servicio;
    private Usuario ana;
    private Usuario luis;

    @Before
    public void preparar() {
        catalogo = Catalogo.getInstancia();
        catalogo.vaciar();
        catalogo.registrar(TipoRecurso.LIBRO, "Clean Code", "Martin");       // L-001
        catalogo.registrar(TipoRecurso.LIBRO, "Refactoring", "Fowler");      // L-002
        catalogo.registrar(TipoRecurso.LIBRO, "Patrones", "Gamma");          // L-003
        catalogo.registrar(TipoRecurso.LIBRO, "Java", "Schildt");            // L-004
        catalogo.registrar(TipoRecurso.EQUIPO, "Portátil", "HP");            // E-001
        catalogo.registrar(TipoRecurso.EQUIPO, "Proyector", "Epson");        // E-002
        reloj = new Reloj(LocalDate.of(2026, 11, 1));
        multas = new CalculadoraMultas();
        servicio = new PrestamoServicio(catalogo, new PrestamoDAOMemoria(), multas,
                new GestorListasEspera(), reloj);
        ana = new Usuario("ana", "Ana");
        luis = new Usuario("luis", "Luis");
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
    public void prestarUsaElPlazoDelTipoDeRecurso() {
        Prestamo libro = servicio.prestar("L-001", ana);
        Prestamo equipo = servicio.prestar("E-001", ana);
        assertEquals(LocalDate.of(2026, 11, 15), libro.getFechaVencimiento());   // 14 días
        assertEquals(LocalDate.of(2026, 11, 4), equipo.getFechaVencimiento());   // 3 días
        assertFalse(catalogo.obtener("L-001").estaDisponible());
    }

    @Test
    public void noPrestaUnRecursoYaPrestado() {
        servicio.prestar("L-001", ana);
        esperaError(() -> servicio.prestar("L-001", luis), "lista de espera");
    }

    @Test
    public void respetaElMaximoDePrestamosPorTipo() {
        servicio.prestar("L-001", ana);
        servicio.prestar("L-002", ana);
        servicio.prestar("L-003", ana);
        esperaError(() -> servicio.prestar("L-004", ana), "Límite alcanzado");   // libros: máximo 3
        servicio.prestar("E-001", ana);                                           // equipo es otro tipo
        esperaError(() -> servicio.prestar("E-002", ana), "Límite alcanzado");   // equipos: máximo 1
    }

    @Test
    public void devolverATiempoNoTieneMultaYLiberaElRecurso() {
        Prestamo p = servicio.prestar("L-001", ana);
        reloj.avanzarDias(10);
        servicio.devolver(p.getId());
        assertEquals(0, p.getMulta());
        assertTrue(catalogo.obtener("L-001").estaDisponible());
        assertFalse(p.estaActivo());
    }

    @Test
    public void devolverTardeCobraLaMultaDeLaPoliticaActiva() {
        Prestamo p = servicio.prestar("L-001", ana);       // vence 15/nov
        reloj.avanzarDias(14 + 5);                         // se devuelve con 5 días de retraso
        servicio.devolver(p.getId());
        assertEquals(5000, p.getMulta());                  // política por defecto: $1.000/día

        Prestamo q = servicio.prestar("L-002", ana);
        multas.setPolitica(new MultaProgresiva());         // cambia la estrategia sin tocar el servicio
        reloj.avanzarDias(14 + 5);
        servicio.devolver(q.getId());
        assertEquals(3500, q.getMulta());
    }

    @Test
    public void noSePuedeDevolverDosVeces() {
        Prestamo p = servicio.prestar("L-001", ana);
        servicio.devolver(p.getId());
        esperaError(() -> servicio.devolver(p.getId()), "ya fue devuelto");
    }

    @Test
    public void renovarExtiendeElVencimientoHastaElMaximo() {
        Prestamo p = servicio.prestar("L-001", ana);       // vence 15/nov, libros: 2 renovaciones
        servicio.renovar(p.getId());
        assertEquals(LocalDate.of(2026, 11, 29), p.getFechaVencimiento());
        servicio.renovar(p.getId());
        assertEquals(2, p.getRenovaciones());
        esperaError(() -> servicio.renovar(p.getId()), "renovación");
    }

    @Test
    public void losEquiposSoloSeRenuevanUnaVez() {
        Prestamo p = servicio.prestar("E-001", ana);
        servicio.renovar(p.getId());
        esperaError(() -> servicio.renovar(p.getId()), "renovación");
    }

    @Test
    public void noSeRenuevaUnPrestamoVencido() {
        Prestamo p = servicio.prestar("L-001", ana);
        reloj.avanzarDias(20);
        esperaError(() -> servicio.renovar(p.getId()), "vencido");
    }

    @Test
    public void alDevolverSeAvisaSoloAlPrimeroDeLaListaDeEspera() {
        Prestamo p = servicio.prestar("L-001", ana);
        Usuario sofia = new Usuario("sofia", "Sofía");
        servicio.unirseListaEspera("L-001", luis);
        servicio.unirseListaEspera("L-001", sofia);
        assertEquals(2, servicio.cantidadEnEspera("L-001"));

        servicio.devolver(p.getId());

        assertEquals(1, luis.getAvisos().size());
        assertTrue(luis.getAvisos().get(0).contains("Clean Code"));
        assertEquals(0, sofia.getAvisos().size());
        assertEquals(1, servicio.cantidadEnEspera("L-001"));
    }

    @Test
    public void noSeRenuevaSiOtroUsuarioEstaEsperando() {
        Prestamo p = servicio.prestar("L-001", ana);
        servicio.unirseListaEspera("L-001", luis);
        esperaError(() -> servicio.renovar(p.getId()), "lista de espera");
    }

    @Test
    public void noSeEntraALaListaSiElRecursoEstaDisponible() {
        esperaError(() -> servicio.unirseListaEspera("L-001", luis), "no hace falta esperar");
    }

    @Test
    public void quienPrestaElRecursoSaleDeLaListaDeEspera() {
        Prestamo p = servicio.prestar("L-001", ana);
        servicio.unirseListaEspera("L-001", luis);
        servicio.devolver(p.getId());                       // luis recibe el aviso y sale de la fila
        servicio.prestar("L-001", luis);
        assertEquals(0, servicio.cantidadEnEspera("L-001"));
    }

    @Test
    public void prestamosActivosDeUnUsuarioSoloIncluyeLosSuyos() {
        servicio.prestar("L-001", ana);
        servicio.prestar("L-002", luis);
        assertEquals(1, servicio.prestamosActivosDe("ana").size());
        assertEquals(2, servicio.prestamosActivos().size());
        assertEquals(2, servicio.historial().size());
    }
}
