package com.biblioteca.dao;

import com.biblioteca.modelo.Recurso;
import java.util.List;
import java.util.Optional;

/**
 * PATRÓN DAO (acceso a datos) — separa "cómo se guardan los recursos" de la lógica
 * del negocio. Hoy se guardan en memoria; mañana podría ser una base de datos
 * creando otra implementación, sin tocar el resto del sistema.
 */
public interface RecursoDAO {

    void guardar(Recurso recurso);

    Optional<Recurso> buscarPorId(String id);

    /** Todos los recursos, ordenados por código. */
    List<Recurso> listar();

    void vaciar();
}
