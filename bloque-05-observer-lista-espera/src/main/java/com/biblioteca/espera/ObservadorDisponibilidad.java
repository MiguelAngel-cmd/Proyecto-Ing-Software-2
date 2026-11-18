package com.biblioteca.espera;

import com.biblioteca.modelo.Recurso;

/**
 * PATRÓN OBSERVER (comportamiento) — rol: Observer.
 * Quien espera un recurso se registra como observador y la lista de espera le
 * avisa cuando el recurso queda disponible. La lista no sabe cómo se entera
 * cada observador (consola, ventana, correo...).
 */
public interface ObservadorDisponibilidad {

    /** Código del usuario que espera (sirve para no repetirlo en la lista). */
    String getIdUsuario();

    /** La lista de espera llama a este método cuando el recurso ya está disponible. */
    void recursoDisponible(Recurso recurso);
}
