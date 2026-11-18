package com.biblioteca.reglas;

/**
 * Reglas de préstamo de un tipo de recurso. Cada tipo de recurso tiene la suya,
 * así el resto del sistema pregunta a la regla en lugar de usar "if (tipo == ...)".
 */
public interface ReglaPrestamo {

    /** Días que dura un préstamo (y cada renovación). */
    int getPlazoDias();

    /** Cuántas veces se puede renovar un mismo préstamo. */
    int getMaxRenovaciones();

    /** Cuántos recursos de este tipo puede tener prestados a la vez un usuario. */
    int getMaxPrestamosPorUsuario();
}
