package com.biblioteca.dto;

import com.biblioteca.modelo.Usuario;

/** DTO de usuario: solo lo que las pantallas necesitan mostrar. */
public record UsuarioDTO(String id, String nombre) {

    public static UsuarioDTO de(Usuario u) {
        return new UsuarioDTO(u.getId(), u.getNombre());
    }
}
