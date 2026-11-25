package com.biblioteca.servicio;

import com.biblioteca.catalogo.Catalogo;
import com.biblioteca.excepcion.BibliotecaException;
import com.biblioteca.modelo.Prestamo;
import com.biblioteca.modelo.Recurso;
import com.biblioteca.modelo.TipoRecurso;
import com.biblioteca.modelo.Usuario;
import com.biblioteca.multas.PoliticaMulta;
import com.biblioteca.util.Reloj;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Patrón Facade: la única puerta de entrada al sistema. La consola y la ventana
 * solo llaman a esta clase; no conocen el catálogo, los servicios ni las políticas.
 */
public class BibliotecaFachada {
    private final Catalogo catalogo = Catalogo.getInstancia();
    private final Map<String, Usuario> usuarios = new TreeMap<>();
    private final Reloj reloj;
    private final PrestamoServicio prestamos;

    public BibliotecaFachada() {
        this(new Reloj());
    }

    public BibliotecaFachada(Reloj reloj) {
        this.reloj = reloj;
        this.prestamos = new PrestamoServicio(catalogo, reloj);
    }

    // ---- usuarios
    public void registrarUsuario(Usuario usuario) { usuarios.put(usuario.getId(), usuario); }
    public List<Usuario> listarUsuarios() { return List.copyOf(usuarios.values()); }

    // ---- catálogo
    public List<Recurso> listarRecursos() { return catalogo.listar(); }
    public Recurso registrarRecurso(TipoRecurso tipo, String titulo, String detalle) { return catalogo.registrar(tipo, titulo, detalle); }
    public Recurso modificarRecurso(String id, String titulo, String detalle) { return catalogo.modificar(id, titulo, detalle); }
    public Recurso darDeBajaRecurso(String id) { return catalogo.darDeBaja(id); }

    // ---- préstamos
    public Prestamo prestar(String idRecurso, String idUsuario) { return prestamos.prestar(idRecurso, usuario(idUsuario)); }
    public Prestamo devolver(int idPrestamo) { return prestamos.devolver(idPrestamo); }
    public Prestamo renovar(int idPrestamo) { return prestamos.renovar(idPrestamo); }
    public List<Prestamo> prestamosActivos() { return prestamos.activos(); }
    public void unirseListaEspera(String idRecurso, String idUsuario) { prestamos.unirseListaEspera(idRecurso, usuario(idUsuario)); }
    public int cantidadEnEspera(String idRecurso) { return prestamos.cantidadEnEspera(idRecurso); }

    // ---- multas
    public List<PoliticaMulta> politicasDisponibles() { return PoliticaMulta.TODAS; }
    public PoliticaMulta politicaActual() { return prestamos.getPolitica(); }

    public void cambiarPolitica(int indice) {
        if (indice < 0 || indice >= PoliticaMulta.TODAS.size()) throw new BibliotecaException("Política de multas inexistente.");
        prestamos.setPolitica(PoliticaMulta.TODAS.get(indice));
    }

    // ---- avisos de la lista de espera
    public List<String> avisos(String idUsuario) { return usuario(idUsuario).getAvisos(); }
    public void limpiarAvisos(String idUsuario) { usuario(idUsuario).limpiarAvisos(); }

    // ---- fecha del sistema (para demostrar multas)
    public LocalDate hoy() { return reloj.hoy(); }

    public void avanzarDias(int dias) {
        if (dias < 1) throw new BibliotecaException("Los días a avanzar deben ser 1 o más.");
        reloj.avanzarDias(dias);
    }

    private Usuario usuario(String id) {
        Usuario u = id == null ? null : usuarios.get(id.trim().toLowerCase());
        if (u == null) throw new BibliotecaException("No existe el usuario '" + id + "'.");
        return u;
    }
}
