package com.biblioteca;

import com.biblioteca.modelo.*;

public class Prueba {
    public static void main(String[] args) {
        Recurso libro = new Libro("L-001", "Clean Code", "Robert C. Martin");
        Recurso equipo = new Equipo("E-001", "Portátil", "Lenovo");
        for (Recurso r : new Recurso[] {libro, equipo}) {
            System.out.println(r.getId() + " " + r.getTitulo() + " | " + r.getEtiquetaDetalle() + ": " + r.getDetalle()
                    + " | plazo " + r.getRegla().getPlazoDias() + " días, renovaciones "
                    + r.getRegla().getMaxRenovaciones());
        }
        libro.marcarPrestado();
        System.out.println("Estado del libro: " + libro.getEstado());
        try {
            libro.marcarPrestado();
        } catch (Exception e) {
            System.out.println("Error esperado: " + e.getMessage());
        }
    }
}
