package com.biblioteca.modelo;

import com.biblioteca.excepcion.BibliotecaException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/** Un préstamo de un recurso a un usuario, con sus fechas, renovaciones y multa. */
public class Prestamo {

    private final int id;
    private final Recurso recurso;
    private final Usuario usuario;
    private final LocalDate fechaPrestamo;
    private LocalDate fechaVencimiento;
    private int renovaciones;
    private LocalDate fechaDevolucion;
    private long multa;

    public Prestamo(int id, Recurso recurso, Usuario usuario, LocalDate fechaPrestamo, LocalDate fechaVencimiento) {
        this.id = id;
        this.recurso = recurso;
        this.usuario = usuario;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaVencimiento = fechaVencimiento;
    }

    public int getId() {
        return id;
    }

    public Recurso getRecurso() {
        return recurso;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public int getRenovaciones() {
        return renovaciones;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public long getMulta() {
        return multa;
    }

    public boolean estaActivo() {
        return fechaDevolucion == null;
    }

    /** Días de retraso respecto a una fecha de referencia (0 si no está vencido). */
    public long diasRetraso(LocalDate referencia) {
        return Math.max(0, ChronoUnit.DAYS.between(fechaVencimiento, referencia));
    }

    /** Extiende el vencimiento {@code dias} días y cuenta una renovación más. */
    public void renovar(int dias) {
        fechaVencimiento = fechaVencimiento.plusDays(dias);
        renovaciones++;
    }

    public void registrarDevolucion(LocalDate fecha, long multaCalculada) {
        if (!estaActivo()) {
            throw new BibliotecaException("El préstamo " + id + " ya fue devuelto.");
        }
        this.fechaDevolucion = fecha;
        this.multa = multaCalculada;
    }
}
