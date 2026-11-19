package com.biblioteca.consola;

import static org.junit.Assert.assertTrue;

import com.biblioteca.app.CargaInicial;
import com.biblioteca.servicio.BibliotecaFachada;
import com.biblioteca.util.Reloj;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import org.junit.Test;

public class MenuConsolaTest {

    /** Ejecuta el menú con una "conversación" escrita y devuelve todo lo que imprimió. */
    private String ejecutar(String... lineas) {
        BibliotecaFachada fachada = new BibliotecaFachada(new Reloj(LocalDate.of(2026, 11, 1)));
        CargaInicial.cargar(fachada);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream salida = new PrintStream(bytes, true, StandardCharsets.UTF_8);
        String entrada = String.join("\n", lineas) + "\n";
        new MenuConsola(fachada, new ByteArrayInputStream(entrada.getBytes(StandardCharsets.UTF_8)), salida).iniciar();
        return bytes.toString(StandardCharsets.UTF_8);
    }

    @Test
    public void prestaYDevuelveConMulta() {
        String salida = ejecutar(
                "6", "ana", "L-001",          // prestar
                "13", "17",                   // adelantar 17 días (el libro vence a los 14)
                "7", "1",                     // devolver el préstamo 1
                "0");
        assertTrue(salida, salida.contains("Préstamo #1: Clean Code a Ana Gómez. Vence el 2026-11-15."));
        assertTrue(salida, salida.contains("Multa por 3 día(s) de retraso"));
        assertTrue(salida, salida.contains("$3.000"));
    }

    @Test
    public void listaDeEsperaYAvisoCuandoSeDevuelve() {
        String salida = ejecutar(
                "6", "ana", "L-001",
                "10", "luis", "L-001",
                "7", "1",
                "11", "luis",
                "0");
        assertTrue(salida, salida.contains("Agregado a la lista de espera"));
        assertTrue(salida, salida.contains("ya está disponible"));
    }

    @Test
    public void registraUnRecursoNuevo() {
        String salida = ejecutar("3", "1", "Java 21", "Schildt", "2", "java", "0");
        assertTrue(salida, salida.contains("Registrado: L-005 - Java 21"));
        assertTrue(salida, salida.contains("Autor: Schildt"));
    }

    @Test
    public void cambiaLaPoliticaDeMultas() {
        String salida = ejecutar("12", "2", "0");
        assertTrue(salida, salida.contains("Política activa: Progresiva"));
    }

    @Test
    public void unaEntradaInvalidaNoRompeElMenu() {
        String salida = ejecutar("99", "7", "abc", "6", "nadie", "L-001", "0");
        assertTrue(salida.contains("Opción no válida."));
        assertTrue(salida.contains("ERROR: debe escribir un número."));
        assertTrue(salida.contains("No existe el usuario"));
        assertTrue(salida.contains("Hasta pronto."));
    }
}
