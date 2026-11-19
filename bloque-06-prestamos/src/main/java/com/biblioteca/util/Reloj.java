package com.biblioteca.util;

import java.time.LocalDate;

/**
 * Fecha "oficial" del sistema. Permite adelantar días para probar multas y
 * vencimientos sin esperar semanas (útil en la demostración y en las pruebas).
 */
public class Reloj {

    private LocalDate fecha;

    /** Reloj que arranca en la fecha actual. */
    public Reloj() {
        this(LocalDate.now());
    }

    public Reloj(LocalDate fechaInicial) {
        this.fecha = fechaInicial;
    }

    public synchronized LocalDate hoy() {
        return fecha;
    }

    public synchronized void avanzarDias(int dias) {
        fecha = fecha.plusDays(dias);
    }
}
