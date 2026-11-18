package com.biblioteca.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/** Persona que usa la biblioteca. Guarda también los avisos que le llegan. */
public class Usuario {

    private final String id;
    private final String nombre;
    private final List<String> avisos = new CopyOnWriteArrayList<>();

    public Usuario(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    /** Guarda un aviso para el usuario (por ejemplo, "el recurso ya está disponible"). */
    public void recibirAviso(String mensaje) {
        avisos.add(mensaje);
    }

    /** Copia de los avisos recibidos; modificarla no afecta al usuario. */
    public List<String> getAvisos() {
        return new ArrayList<>(avisos);
    }

    public void limpiarAvisos() {
        avisos.clear();
    }
}
