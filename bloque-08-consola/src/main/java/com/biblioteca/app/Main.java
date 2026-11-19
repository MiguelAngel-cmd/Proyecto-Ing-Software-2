package com.biblioteca.app;

import com.biblioteca.consola.MenuConsola;
import com.biblioteca.servicio.BibliotecaFachada;

/** Programa principal (consola): carga datos de ejemplo y abre el menú. */
public class Main {

    public static void main(String[] args) {
        BibliotecaFachada fachada = new BibliotecaFachada();
        CargaInicial.cargar(fachada);
        new MenuConsola(fachada, System.in, System.out).iniciar();
    }
}
