package com.biblioteca.dao.memoria;

import com.biblioteca.dao.PrestamoDAO;
import com.biblioteca.modelo.Prestamo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

/** Implementación en memoria del DAO de préstamos. */
public class PrestamoDAOMemoria implements PrestamoDAO {

    private final ConcurrentSkipListMap<Integer, Prestamo> prestamos = new ConcurrentSkipListMap<>();
    private final AtomicInteger contador = new AtomicInteger();

    @Override
    public int siguienteId() {
        return contador.incrementAndGet();
    }

    @Override
    public void guardar(Prestamo prestamo) {
        prestamos.put(prestamo.getId(), prestamo);
    }

    @Override
    public Optional<Prestamo> buscarPorId(int id) {
        return Optional.ofNullable(prestamos.get(id));
    }

    @Override
    public List<Prestamo> listar() {
        return new ArrayList<>(prestamos.values());
    }

    @Override
    public void vaciar() {
        prestamos.clear();
        contador.set(0);
    }
}
