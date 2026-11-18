package com.biblioteca;

import com.biblioteca.modelo.*;
import com.biblioteca.multas.*;

public class Prueba {
    public static void main(String[] args) {
        Recurso libro = new Libro("L-001", "Clean Code", "Martin");
        Recurso equipo = new Equipo("E-001", "Portátil", "HP");
        CalculadoraMultas calc = new CalculadoraMultas();
        for (PoliticaMulta politica : PoliticasDeMulta.todas()) {
            calc.setPolitica(politica);   // se cambia la estrategia; la calculadora no cambia
            System.out.println(politica.getNombre() + ": libro con 5 días de retraso = $" + calc.calcular(5, libro)
                    + " | equipo con 2 días = $" + calc.calcular(2, equipo));
        }
        System.out.println("Sin retraso: $" + calc.calcular(0, libro));
    }
}
