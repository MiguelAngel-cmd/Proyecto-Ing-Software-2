package com.biblioteca;

import com.biblioteca.catalogo.Catalogo;
import com.biblioteca.dao.memoria.PrestamoDAOMemoria;
import com.biblioteca.espera.GestorListasEspera;
import com.biblioteca.modelo.*;
import com.biblioteca.multas.CalculadoraMultas;
import com.biblioteca.servicio.PrestamoServicio;
import com.biblioteca.util.Reloj;
import java.time.LocalDate;

public class Prueba {
    public static void main(String[] args) {
        Catalogo catalogo = Catalogo.getInstancia();
        catalogo.registrar(TipoRecurso.LIBRO, "Clean Code", "Martin");
        Reloj reloj = new Reloj(LocalDate.of(2026, 11, 1));
        PrestamoServicio servicio = new PrestamoServicio(catalogo, new PrestamoDAOMemoria(),
                new CalculadoraMultas(), new GestorListasEspera(), reloj);
        Usuario ana = new Usuario("ana", "Ana");
        Usuario luis = new Usuario("luis", "Luis");

        Prestamo p = servicio.prestar("L-001", ana);
        System.out.println("Préstamo #" + p.getId() + " a Ana. Vence el " + p.getFechaVencimiento());
        servicio.unirseListaEspera("L-001", luis);
        try {
            servicio.renovar(p.getId());
        } catch (Exception e) {
            System.out.println("Error esperado: " + e.getMessage());
        }
        reloj.avanzarDias(19);            // se devuelve 5 días tarde
        servicio.devolver(p.getId());
        System.out.println("Devuelto con " + p.diasRetraso(p.getFechaDevolucion()) + " días de retraso. Multa: $" + p.getMulta());
        System.out.println("Avisos de Luis: " + luis.getAvisos());
    }
}
