package com.biblioteca.dao.memoria;

import com.biblioteca.dao.UsuarioDAO;
import com.biblioteca.modelo.Usuario;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;

/** Implementación en memoria del DAO de usuarios. */
public class UsuarioDAOMemoria implements UsuarioDAO {

    private final ConcurrentSkipListMap<String, Usuario> usuarios = new ConcurrentSkipListMap<>();

    @Override
    public void guardar(Usuario usuario) {
        usuarios.put(usuario.getId(), usuario);
    }

    @Override
    public Optional<Usuario> buscarPorId(String id) {
        return Optional.ofNullable(id == null ? null : usuarios.get(id.trim().toLowerCase()));
    }

    @Override
    public List<Usuario> listar() {
        return new ArrayList<>(usuarios.values());
    }
}
