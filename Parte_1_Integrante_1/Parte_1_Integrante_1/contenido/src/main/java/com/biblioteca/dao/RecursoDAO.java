package com.biblioteca.dao;

import com.biblioteca.modelo.Recurso;
import java.util.List;
import java.util.Optional;

/** Acceso a los datos de los recursos. Hoy es en memoria; mañana podría ser un archivo o una base de datos. */
public interface RecursoDAO {
    void guardar(Recurso recurso);
    Optional<Recurso> buscarPorId(String id);
    List<Recurso> listar();
    void vaciar();
}
