package com.biblioteca.reglas;

/** Reglas para otro recurso: 7 días de plazo, 1 renovación(es) y máximo 2 a la vez por usuario. */
public class ReglaOtro implements ReglaPrestamo {

    @Override
    public int getPlazoDias() {
        return 7;
    }

    @Override
    public int getMaxRenovaciones() {
        return 1;
    }

    @Override
    public int getMaxPrestamosPorUsuario() {
        return 2;
    }
}
