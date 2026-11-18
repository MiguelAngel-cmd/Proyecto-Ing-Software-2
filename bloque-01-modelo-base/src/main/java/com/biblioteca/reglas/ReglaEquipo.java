package com.biblioteca.reglas;

/** Reglas para un equipo: 3 días de plazo, 1 renovación(es) y máximo 1 a la vez por usuario. */
public class ReglaEquipo implements ReglaPrestamo {

    @Override
    public int getPlazoDias() {
        return 3;
    }

    @Override
    public int getMaxRenovaciones() {
        return 1;
    }

    @Override
    public int getMaxPrestamosPorUsuario() {
        return 1;
    }
}
