package com.biblioteca.dao;

import com.biblioteca.modelo.Prestamo;
import java.util.List;
import java.util.Optional;

/** DAO de préstamos: guarda y consulta préstamos sin importar dónde se almacenen. */
public interface PrestamoDAO {

    /** Entrega el siguiente número de préstamo disponible. */
    int siguienteId();

    void guardar(Prestamo prestamo);

    Optional<Prestamo> buscarPorId(int id);

    /** Todos los préstamos (activos y devueltos), del más antiguo al más reciente. */
    List<Prestamo> listar();

    void vaciar();
}
