package com.biblioteca.dto;

import com.biblioteca.modelo.Prestamo;
import com.biblioteca.util.Dinero;
import java.time.LocalDate;

/**
 * DTO de préstamo. {@code multa} es la multa cobrada si ya fue devuelto, o la
 * multa estimada a la fecha de hoy si sigue activo y está vencido.
 */
public record PrestamoDTO(int id, String idRecurso, String tituloRecurso, String tipo,
                          String idUsuario, String nombreUsuario,
                          LocalDate fechaPrestamo, LocalDate fechaVencimiento, LocalDate fechaDevolucion,
                          int renovaciones, int maxRenovaciones, boolean activo,
                          long diasRetraso, long multa) {

    public static PrestamoDTO de(Prestamo p, long diasRetraso, long multa) {
        return new PrestamoDTO(p.getId(), p.getRecurso().getId(), p.getRecurso().getTitulo(),
                p.getRecurso().getTipo().getNombre(), p.getUsuario().getId(), p.getUsuario().getNombre(),
                p.getFechaPrestamo(), p.getFechaVencimiento(), p.getFechaDevolucion(),
                p.getRenovaciones(), p.getRecurso().getRegla().getMaxRenovaciones(), p.estaActivo(),
                diasRetraso, multa);
    }

    public String estado() {
        if (!activo) {
            return diasRetraso > 0 ? "Devuelto tarde (" + diasRetraso + " d)" : "Devuelto";
        }
        return diasRetraso > 0 ? "VENCIDO (" + diasRetraso + " d)" : "Activo";
    }

    /** Una línea lista para mostrar en consola. */
    public String resumen() {
        String fin = activo ? "vence " + fechaVencimiento : "devuelto " + fechaDevolucion;
        String dinero = multa > 0 ? " | multa " + Dinero.formato(multa) : "";
        return String.format("#%-3d %-6s %-26s %-12s %s | renov. %d/%d | %s%s", id, idRecurso, tituloRecurso,
                nombreUsuario, fin, renovaciones, maxRenovaciones, estado(), dinero);
    }
}
