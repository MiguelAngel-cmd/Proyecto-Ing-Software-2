package com.biblioteca.servicio;

import com.biblioteca.catalogo.Catalogo;
import com.biblioteca.dao.UsuarioDAO;
import com.biblioteca.dao.memoria.PrestamoDAOMemoria;
import com.biblioteca.dao.memoria.UsuarioDAOMemoria;
import com.biblioteca.dto.PrestamoDTO;
import com.biblioteca.dto.RecursoDTO;
import com.biblioteca.dto.UsuarioDTO;
import com.biblioteca.espera.GestorListasEspera;
import com.biblioteca.excepcion.BibliotecaException;
import com.biblioteca.modelo.Prestamo;
import com.biblioteca.modelo.Recurso;
import com.biblioteca.modelo.TipoRecurso;
import com.biblioteca.modelo.Usuario;
import com.biblioteca.multas.CalculadoraMultas;
import com.biblioteca.multas.PoliticaMulta;
import com.biblioteca.multas.PoliticasDeMulta;
import com.biblioteca.util.Reloj;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * PATRÓN FACADE (estructural): la consola y la ventana hablan SOLO con esta clase.
 * Ella arma el núcleo (catálogo, préstamos, multas, lista de espera) y devuelve
 * DTO. Por eso las dos interfaces comparten exactamente la misma lógica.
 */
public class BibliotecaFachada {

    private final Catalogo catalogo = Catalogo.getInstancia();
    private final UsuarioDAO usuarios = new UsuarioDAOMemoria();
    private final CalculadoraMultas multas = new CalculadoraMultas();
    private final List<PoliticaMulta> politicas = PoliticasDeMulta.todas();
    private final Reloj reloj;
    private final PrestamoServicio prestamos;

    public BibliotecaFachada() {
        this(new Reloj());
    }

    public BibliotecaFachada(Reloj reloj) {
        this.reloj = reloj;
        this.prestamos = new PrestamoServicio(catalogo, new PrestamoDAOMemoria(), multas,
                new GestorListasEspera(), reloj);
    }

    // --------------------------------------------------------------- usuarios

    public void registrarUsuario(Usuario usuario) {
        usuarios.guardar(usuario);
    }

    public List<UsuarioDTO> listarUsuarios() {
        List<UsuarioDTO> lista = new ArrayList<>();
        for (Usuario u : usuarios.listar()) {
            lista.add(UsuarioDTO.de(u));
        }
        return lista;
    }

    // -------------------------------------------------------------- catálogo

    public List<RecursoDTO> listarRecursos() {
        return aDto(catalogo.listar());
    }

    public List<RecursoDTO> buscarRecursos(String texto) {
        return aDto(catalogo.buscar(texto));
    }

    public RecursoDTO registrarRecurso(TipoRecurso tipo, String titulo, String detalle) {
        return RecursoDTO.de(catalogo.registrar(tipo, titulo, detalle));
    }

    public RecursoDTO modificarRecurso(String idRecurso, String titulo, String detalle) {
        return RecursoDTO.de(catalogo.modificar(idRecurso, titulo, detalle));
    }

    public RecursoDTO darDeBajaRecurso(String idRecurso) {
        return RecursoDTO.de(catalogo.darDeBaja(idRecurso));
    }

    // ------------------------------------------------------------- préstamos

    public PrestamoDTO prestar(String idRecurso, String idUsuario) {
        return aDto(prestamos.prestar(idRecurso, buscarUsuario(idUsuario)));
    }

    public PrestamoDTO devolver(int idPrestamo) {
        return aDto(prestamos.devolver(idPrestamo));
    }

    public PrestamoDTO renovar(int idPrestamo) {
        return aDto(prestamos.renovar(idPrestamo));
    }

    public void unirseListaEspera(String idRecurso, String idUsuario) {
        prestamos.unirseListaEspera(idRecurso, buscarUsuario(idUsuario));
    }

    public int cantidadEnEspera(String idRecurso) {
        return prestamos.cantidadEnEspera(idRecurso);
    }

    public List<PrestamoDTO> prestamosActivos() {
        return aDtoPrestamos(prestamos.prestamosActivos());
    }

    public List<PrestamoDTO> historial() {
        return aDtoPrestamos(prestamos.historial());
    }

    // ----------------------------------------------------------------- multas

    public List<PoliticaMulta> politicasDisponibles() {
        return List.copyOf(politicas);
    }

    public PoliticaMulta politicaActual() {
        return multas.getPolitica();
    }

    public void cambiarPolitica(int indice) {
        if (indice < 0 || indice >= politicas.size()) {
            throw new BibliotecaException("Política de multas inexistente.");
        }
        multas.setPolitica(politicas.get(indice));
    }

    // ----------------------------------------------------------------- avisos

    public List<String> avisos(String idUsuario) {
        return buscarUsuario(idUsuario).getAvisos();
    }

    public void limpiarAvisos(String idUsuario) {
        buscarUsuario(idUsuario).limpiarAvisos();
    }

    // ------------------------------------------------------------------ fecha

    public LocalDate hoy() {
        return reloj.hoy();
    }

    /** Adelanta la fecha del sistema, para probar vencimientos y multas sin esperar. */
    public void avanzarDias(int dias) {
        if (dias < 1) {
            throw new BibliotecaException("Los días a avanzar deben ser 1 o más.");
        }
        reloj.avanzarDias(dias);
    }

    // ---------------------------------------------------------------- privados

    private Usuario buscarUsuario(String idUsuario) {
        return usuarios.buscarPorId(idUsuario)
                .orElseThrow(() -> new BibliotecaException("No existe el usuario '" + idUsuario + "'."));
    }

    private List<RecursoDTO> aDto(List<Recurso> recursos) {
        List<RecursoDTO> lista = new ArrayList<>();
        for (Recurso r : recursos) {
            lista.add(RecursoDTO.de(r));
        }
        return lista;
    }

    private List<PrestamoDTO> aDtoPrestamos(List<Prestamo> lista) {
        List<PrestamoDTO> dtos = new ArrayList<>();
        for (Prestamo p : lista) {
            dtos.add(aDto(p));
        }
        return dtos;
    }

    private PrestamoDTO aDto(Prestamo p) {
        if (!p.estaActivo()) {
            return PrestamoDTO.de(p, p.diasRetraso(p.getFechaDevolucion()), p.getMulta());
        }
        long dias = p.diasRetraso(reloj.hoy());
        return PrestamoDTO.de(p, dias, multas.calcular(dias, p.getRecurso()));
    }
}
