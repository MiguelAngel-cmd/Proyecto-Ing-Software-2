package com.biblioteca.dao.memoria;

import com.biblioteca.dao.RecursoDAO;
import com.biblioteca.modelo.Recurso;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

public class RecursoDAOMemoria implements RecursoDAO {
    private final TreeMap<String, Recurso> recursos = new TreeMap<>();

    @Override public void guardar(Recurso recurso) { recursos.put(recurso.getId(), recurso); }
    @Override public Optional<Recurso> buscarPorId(String id) { return Optional.ofNullable(recursos.get(id)); }
    @Override public List<Recurso> listar() { return new ArrayList<>(recursos.values()); }
    @Override public void vaciar() { recursos.clear(); }
}
