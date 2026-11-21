package com.biblioteca.modelo;

import com.biblioteca.espera.ObservadorDisponibilidad;
import java.util.ArrayList;
import java.util.List;

/** Persona que usa la biblioteca. Es el Observer: cuando le toca el turno, recibe un aviso. */
public class Usuario implements ObservadorDisponibilidad {
    private final String id;
    private final String nombre;
    private final List<String> avisos = new ArrayList<>();

    public Usuario(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    @Override public String getId() { return id; }
    public String getNombre() { return nombre; }
    public List<String> getAvisos() { return new ArrayList<>(avisos); }
    public void limpiarAvisos() { avisos.clear(); }

    @Override
    public void recursoDisponible(Recurso recurso) {
        avisos.add("'" + recurso.getTitulo() + "' (" + recurso.getId() + ") ya está disponible. Acérquese a pedirlo.");
    }
}
