package com.biblioteca;

import com.biblioteca.fabrica.*;
import com.biblioteca.modelo.*;

public class Prueba {
    public static void main(String[] args) {
        for (TipoRecurso tipo : TipoRecurso.values()) {
            Recurso r = FabricasDeRecurso.para(tipo)
                    .crear(new DatosRecurso(tipo.getPrefijoId() + "-001", "Recurso de prueba", "dato"));
            System.out.println(tipo + " -> la fábrica creó un " + r.getClass().getSimpleName()
                    + " | plazo " + r.getRegla().getPlazoDias() + " días");
        }
        try {
            FabricasDeRecurso.para(TipoRecurso.LIBRO).crear(new DatosRecurso("L-002", " ", "x"));
        } catch (Exception e) {
            System.out.println("Error esperado: " + e.getMessage());
        }
    }
}
