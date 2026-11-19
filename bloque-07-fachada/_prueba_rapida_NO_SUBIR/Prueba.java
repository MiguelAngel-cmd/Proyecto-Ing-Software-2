package com.biblioteca;

import com.biblioteca.dto.PrestamoDTO;
import com.biblioteca.modelo.*;
import com.biblioteca.servicio.BibliotecaFachada;
import com.biblioteca.util.Reloj;
import java.time.LocalDate;

public class Prueba {
    public static void main(String[] args) {
        BibliotecaFachada f = new BibliotecaFachada(new Reloj(LocalDate.of(2026, 11, 1)));
        f.registrarUsuario(new Usuario("ana", "Ana"));
        f.registrarUsuario(new Usuario("luis", "Luis"));
        f.registrarRecurso(TipoRecurso.LIBRO, "Clean Code", "Martin");

        PrestamoDTO p = f.prestar("L-001", "ana");
        System.out.println(p.resumen());
        f.unirseListaEspera("L-001", "luis");
        f.avanzarDias(17);                       // se pasa del plazo de 14 días
        System.out.println(f.prestamosActivos().get(0).resumen());
        f.cambiarPolitica(1);                    // política progresiva
        System.out.println(f.devolver(p.id()).resumen());
        System.out.println("Avisos de Luis: " + f.avisos("luis"));
    }
}
