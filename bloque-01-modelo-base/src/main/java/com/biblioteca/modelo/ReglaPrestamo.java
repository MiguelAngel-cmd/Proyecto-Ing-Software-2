package com.biblioteca.modelo;

/** Reglas de préstamo de un tipo de recurso (primera área de variación). */
public record ReglaPrestamo(int plazoDias, int maxRenovaciones, int maxPorUsuario) {
}
