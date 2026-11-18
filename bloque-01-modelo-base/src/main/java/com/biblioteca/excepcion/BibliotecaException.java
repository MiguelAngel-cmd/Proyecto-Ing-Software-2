package com.biblioteca.excepcion;

/**
 * Error de negocio del sistema (regla incumplida, dato inválido, recurso
 * inexistente, etc.). El mensaje está pensado para mostrarse tal cual al usuario.
 */
public class BibliotecaException extends RuntimeException {

    public BibliotecaException(String mensaje) {
        super(mensaje);
    }
}
