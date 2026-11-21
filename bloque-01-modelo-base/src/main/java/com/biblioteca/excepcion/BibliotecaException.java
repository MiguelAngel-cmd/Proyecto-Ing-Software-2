package com.biblioteca.excepcion;

/** Error de negocio (regla incumplida, dato inválido...). Su mensaje se muestra tal cual al usuario. */
public class BibliotecaException extends RuntimeException {
    public BibliotecaException(String mensaje) {
        super(mensaje);
    }
}
