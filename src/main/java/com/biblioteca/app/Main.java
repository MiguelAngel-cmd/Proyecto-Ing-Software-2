package com.biblioteca.app;

import com.biblioteca.consola.MenuConsola;
import com.biblioteca.servicio.BibliotecaFachada;
import com.biblioteca.vista.VentanaPrincipal;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import javax.swing.SwingUtilities;

/** Sin argumentos abre la consola; con el argumento "gui" abre la ventana. Ambas usan la misma fachada. */
public class Main {
    public static void main(String[] args) {
        BibliotecaFachada fachada = new BibliotecaFachada();
        CargaInicial.cargar(fachada);

        if (args.length > 0 && args[0].equalsIgnoreCase("gui")) {
            SwingUtilities.invokeLater(() -> new VentanaPrincipal(fachada).setVisible(true));
        } else {
            PrintStream salida = new PrintStream(System.out, true, StandardCharsets.UTF_8);   // tildes correctas
            new MenuConsola(fachada, System.in, salida).iniciar();
        }
    }
}
