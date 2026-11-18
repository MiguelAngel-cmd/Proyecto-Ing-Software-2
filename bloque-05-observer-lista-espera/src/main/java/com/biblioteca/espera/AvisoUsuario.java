package com.biblioteca.espera;

import com.biblioteca.modelo.Recurso;
import com.biblioteca.modelo.Usuario;

/** Observador concreto (ConcreteObserver): deja un aviso en la bandeja del usuario. */
public class AvisoUsuario implements ObservadorDisponibilidad {

    private final Usuario usuario;

    public AvisoUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String getIdUsuario() {
        return usuario.getId();
    }

    @Override
    public void recursoDisponible(Recurso recurso) {
        usuario.recibirAviso("El recurso " + recurso.getId() + " '" + recurso.getTitulo()
                + "' ya está disponible. Acérquese a pedirlo.");
    }
}
