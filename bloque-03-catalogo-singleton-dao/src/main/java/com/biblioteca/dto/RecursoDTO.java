package com.biblioteca.dto;

import com.biblioteca.modelo.Recurso;

/**
 * PATRÓN DTO (Data Transfer Object): objeto simple e inmutable con los datos de un
 * recurso que viajan hacia la consola y la interfaz gráfica. Las pantallas nunca
 * reciben el objeto de dominio, así no pueden saltarse las reglas del negocio.
 */
public record RecursoDTO(String id, String tipo, String titulo, String etiquetaDetalle, String detalle,
                         String estado, int plazoDias, int maxRenovaciones, int maxPorUsuario) {

    public static RecursoDTO de(Recurso r) {
        return new RecursoDTO(r.getId(), r.getTipo().getNombre(), r.getTitulo(), r.getEtiquetaDetalle(),
                r.getDetalle(), r.getEstado().toString(), r.getRegla().getPlazoDias(),
                r.getRegla().getMaxRenovaciones(), r.getRegla().getMaxPrestamosPorUsuario());
    }

    /** Una línea lista para mostrar en consola. */
    public String resumen() {
        return String.format("%-6s %-8s %-32s %-10s %s", id, tipo, titulo, estado, etiquetaDetalle + ": " + detalle);
    }
}
