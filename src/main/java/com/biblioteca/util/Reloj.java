package com.biblioteca.util;

import java.time.LocalDate;

/** La fecha del sistema. Se puede adelantar para demostrar préstamos vencidos y multas sin esperar días reales. */
public class Reloj {
    private LocalDate fecha;

    public Reloj() { this(LocalDate.now()); }
    public Reloj(LocalDate inicial) { this.fecha = inicial; }

    public LocalDate hoy() { return fecha; }
    public void avanzarDias(int dias) { fecha = fecha.plusDays(dias); }
}
