package com.biblioteca.consola;

import static org.junit.Assert.assertTrue;

import com.biblioteca.app.CargaInicial;
import com.biblioteca.servicio.BibliotecaFachada;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.Test;

public class MenuConsolaTest {

    /** Escribe las opciones como las tecleará una persona y devuelve lo que la consola imprimió. */
    private String ejecutar(String teclas) throws Exception {
        BibliotecaFachada fachada = new BibliotecaFachada();
        CargaInicial.cargar(fachada);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        new MenuConsola(fachada, new ByteArrayInputStream(teclas.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(bytes, true, "UTF-8")).iniciar();
        return bytes.toString("UTF-8");
    }

    @Test
    public void muestraElCatalogoYSale() throws Exception {
        String texto = ejecutar("1\n0\n");
        assertTrue(texto.contains("Clean Code"));
        assertTrue(texto.contains("Hasta pronto."));
    }

    @Test
    public void unErrorDeNegocioSeMuestraSinCerrarElPrograma() throws Exception {
        String texto = ejecutar("5\nana\nZ-999\n0\n");          // prestar un recurso que no existe
        assertTrue(texto.contains("ERROR: No existe un recurso"));
        assertTrue(texto.contains("Hasta pronto."));
    }

    @Test
    public void unPrestamoSeRegistraDesdeLaConsola() throws Exception {
        assertTrue(ejecutar("5\nana\nL-001\n0\n").contains("Préstamo #1: Clean Code a Ana Gómez"));
    }
}
