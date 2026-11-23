package com.biblioteca.modelo;

import com.biblioteca.excepcion.BibliotecaException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Prestamo {
    private final int id;
    private final Recurso recurso;
    private final Usuario usuario;
    private LocalDate fechaVencimiento;
    private LocalDate fechaDevolucion;      // null mientras el préstamo siga activo
    private int renovaciones;
    private long multa;

    public Prestamo(int id, Recurso recurso, Usuario usuario, LocalDate fechaVencimiento) {
        this.id = id;
        this.recurso = recurso;
        this.usuario = usuario;
        this.fechaVencimiento = fechaVencimiento;
    }

    public int getId() { return id; }
    public Recurso getRecurso() { return recurso; }
    public Usuario getUsuario() { return usuario; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public int getRenovaciones() { return renovaciones; }
    public long getMulta() { return multa; }
    public boolean estaActivo() { return fechaDevolucion == null; }

    /** Días de retraso respecto al vencimiento (0 si no hay retraso). */
    public long diasRetraso(LocalDate referencia) {
        return Math.max(0, ChronoUnit.DAYS.between(fechaVencimiento, referencia));
    }

    public void renovar(int dias) {
        fechaVencimiento = fechaVencimiento.plusDays(dias);
        renovaciones++;
    }

    public void registrarDevolucion(LocalDate fecha, long multaCalculada) {
        if (!estaActivo()) throw new BibliotecaException("El préstamo " + id + " ya fue devuelto.");
        this.fechaDevolucion = fecha;
        this.multa = multaCalculada;
    }
}
