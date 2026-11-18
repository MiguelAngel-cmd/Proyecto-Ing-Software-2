package com.biblioteca.catalogo;

import com.biblioteca.dao.RecursoDAO;
import com.biblioteca.dao.memoria.RecursoDAOMemoria;
import com.biblioteca.excepcion.BibliotecaException;
import com.biblioteca.fabrica.DatosRecurso;
import com.biblioteca.fabrica.FabricasDeRecurso;
import com.biblioteca.modelo.EstadoRecurso;
import com.biblioteca.modelo.Recurso;
import com.biblioteca.modelo.TipoRecurso;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * PATRÓN SINGLETON (creacional): existe un único catálogo en toda la aplicación.
 * Así la consola y la interfaz gráfica ven siempre los mismos recursos y no hay
 * copias desincronizadas (RF-01: "catálogo único y consistente").
 *
 * Se obtiene con {@link #getInstancia()}; el constructor es privado.
 */
public final class Catalogo {

    /** Holder: la instancia se crea una sola vez, de forma segura entre hilos. */
    private static final class Holder {
        private static final Catalogo INSTANCIA = new Catalogo();
    }

    private final RecursoDAO dao = new RecursoDAOMemoria();
    private final Map<TipoRecurso, AtomicInteger> contadores = new EnumMap<>(TipoRecurso.class);

    private Catalogo() {
        reiniciarContadores();
    }

    public static Catalogo getInstancia() {
        return Holder.INSTANCIA;
    }

    /** RF-01: registra un recurso nuevo; el código (L-001, E-001...) lo genera el catálogo. */
    public Recurso registrar(TipoRecurso tipo, String titulo, String detalle) {
        String id = tipo.getPrefijoId() + "-" + String.format("%03d", contadores.get(tipo).incrementAndGet());
        Recurso recurso = FabricasDeRecurso.para(tipo).crear(new DatosRecurso(id, titulo, detalle));
        dao.guardar(recurso);
        return recurso;
    }

    /** Busca un recurso por código; lanza error si no existe. */
    public Recurso obtener(String id) {
        return dao.buscarPorId(id == null ? "" : id.trim().toUpperCase(Locale.ROOT))
                .orElseThrow(() -> new BibliotecaException("No existe un recurso con el código '" + id + "'."));
    }

    /** Recursos activos del catálogo (sin los dados de baja). */
    public List<Recurso> listar() {
        List<Recurso> activos = new ArrayList<>();
        for (Recurso r : dao.listar()) {
            if (r.getEstado() != EstadoRecurso.DE_BAJA) {
                activos.add(r);
            }
        }
        return activos;
    }

    /** Busca por texto en el título o en el dato particular (autor, marca, categoría). */
    public List<Recurso> buscar(String texto) {
        String t = texto == null ? "" : texto.trim().toLowerCase(Locale.ROOT);
        List<Recurso> encontrados = new ArrayList<>();
        for (Recurso r : listar()) {
            if (r.getTitulo().toLowerCase(Locale.ROOT).contains(t)
                    || r.getDetalle().toLowerCase(Locale.ROOT).contains(t)) {
                encontrados.add(r);
            }
        }
        return encontrados;
    }

    /** RF-01: modifica título y dato particular de un recurso. */
    public Recurso modificar(String id, String nuevoTitulo, String nuevoDetalle) {
        if (nuevoTitulo == null || nuevoTitulo.isBlank() || nuevoDetalle == null || nuevoDetalle.isBlank()) {
            throw new BibliotecaException("El título y el dato particular no pueden estar vacíos.");
        }
        Recurso recurso = obtener(id);
        recurso.modificar(nuevoTitulo.trim(), nuevoDetalle.trim());
        dao.guardar(recurso);
        return recurso;
    }

    /** RF-01: da de baja un recurso (no puede estar prestado). */
    public Recurso darDeBaja(String id) {
        Recurso recurso = obtener(id);
        recurso.darDeBaja();
        dao.guardar(recurso);
        return recurso;
    }

    /** Solo para pruebas y para recargar datos de demostración: deja el catálogo vacío. */
    public synchronized void vaciar() {
        dao.vaciar();
        reiniciarContadores();
    }

    private void reiniciarContadores() {
        for (TipoRecurso tipo : TipoRecurso.values()) {
            contadores.put(tipo, new AtomicInteger());
        }
    }
}
