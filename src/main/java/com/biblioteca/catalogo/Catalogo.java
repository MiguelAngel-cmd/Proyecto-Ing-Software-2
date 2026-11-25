package com.biblioteca.catalogo;

import com.biblioteca.dao.RecursoDAO;
import com.biblioteca.dao.memoria.RecursoDAOMemoria;
import com.biblioteca.excepcion.BibliotecaException;
import com.biblioteca.fabrica.FabricaRecurso;
import com.biblioteca.modelo.Recurso;
import com.biblioteca.modelo.TipoRecurso;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Patrón Singleton: una sola instancia del catálogo para toda la aplicación.
 * La instancia se crea al cargar la clase, por eso es segura entre hilos.
 */
public final class Catalogo {
    private static final Catalogo INSTANCIA = new Catalogo();

    private final RecursoDAO dao = new RecursoDAOMemoria();
    private final Map<TipoRecurso, Integer> contadores = new EnumMap<>(TipoRecurso.class);

    private Catalogo() { }

    public static Catalogo getInstancia() { return INSTANCIA; }

    public synchronized Recurso registrar(TipoRecurso tipo, String titulo, String detalle) {
        int numero = contadores.getOrDefault(tipo, 0) + 1;
        String id = String.format("%s-%03d", tipo.getPrefijo(), numero);
        Recurso recurso = FabricaRecurso.para(tipo).crear(id, titulo, detalle);   // Factory Method
        contadores.put(tipo, numero);
        dao.guardar(recurso);
        return recurso;
    }

    public Recurso obtener(String id) {
        String codigo = id == null ? "" : id.trim().toUpperCase();
        return dao.buscarPorId(codigo)
                .orElseThrow(() -> new BibliotecaException("No existe un recurso con el código '" + id + "'."));
    }

    /** Recursos que no están dados de baja. */
    public List<Recurso> listar() {
        return dao.listar().stream().filter(r -> !r.estaDeBaja()).toList();
    }

    public Recurso modificar(String id, String titulo, String detalle) {
        if (titulo == null || titulo.isBlank() || detalle == null || detalle.isBlank())
            throw new BibliotecaException("El título y el dato propio no pueden estar vacíos.");
        Recurso recurso = obtener(id);
        recurso.modificar(titulo.trim(), detalle.trim());
        dao.guardar(recurso);
        return recurso;
    }

    public Recurso darDeBaja(String id) {
        Recurso recurso = obtener(id);
        recurso.darDeBaja();
        dao.guardar(recurso);
        return recurso;
    }

    /** Deja el catálogo vacío (lo usan las pruebas y la carga de datos de ejemplo). */
    public synchronized void vaciar() {
        dao.vaciar();
        contadores.clear();
    }
}
