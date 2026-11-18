package com.biblioteca.reglas;

/** Reglas para un libro: 14 días de plazo, 2 renovación(es) y máximo 3 a la vez por usuario. */
public class ReglaLibro implements ReglaPrestamo {

    @Override
    public int getPlazoDias() {
        return 14;
    }

    @Override
    public int getMaxRenovaciones() {
        return 2;
    }

    @Override
    public int getMaxPrestamosPorUsuario() {
        return 3;
    }
}
