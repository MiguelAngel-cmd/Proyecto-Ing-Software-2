package com.biblioteca.dao;

import com.biblioteca.modelo.Usuario;
import java.util.List;
import java.util.Optional;

/** DAO de usuarios. */
public interface UsuarioDAO {

    void guardar(Usuario usuario);

    Optional<Usuario> buscarPorId(String id);

    List<Usuario> listar();
}
