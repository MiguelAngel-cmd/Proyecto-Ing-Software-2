package com.biblioteca;

import com.biblioteca.catalogo.Catalogo;
import com.biblioteca.dto.RecursoDTO;
import com.biblioteca.modelo.*;

public class Prueba {
    public static void main(String[] args) {
        Catalogo c = Catalogo.getInstancia();
        System.out.println("¿Siempre la misma instancia? " + (c == Catalogo.getInstancia()));
        c.registrar(TipoRecurso.LIBRO, "Clean Code", "Robert C. Martin");
        c.registrar(TipoRecurso.LIBRO, "Refactoring", "Martin Fowler");
        c.registrar(TipoRecurso.EQUIPO, "Portátil", "Lenovo");
        for (Recurso r : c.listar()) {
            System.out.println(RecursoDTO.de(r).resumen());
        }
        System.out.println("Búsqueda 'martin': " + c.buscar("martin").size() + " resultado(s)");
        c.darDeBaja("L-002");
        System.out.println("Tras dar de baja L-002 quedan " + c.listar().size() + " recursos activos");
    }
}
