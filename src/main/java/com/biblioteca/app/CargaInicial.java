package com.biblioteca.app;

import com.biblioteca.catalogo.Catalogo;
import com.biblioteca.modelo.TipoRecurso;
import com.biblioteca.modelo.Usuario;
import com.biblioteca.servicio.BibliotecaFachada;

/** Datos de ejemplo para poder probar el sistema sin base de datos. */
public final class CargaInicial {
    private CargaInicial() { }

    public static void cargar(BibliotecaFachada fachada) {
        Catalogo.getInstancia().vaciar();
        fachada.registrarUsuario(new Usuario("ana", "Ana Gómez"));
        fachada.registrarUsuario(new Usuario("luis", "Luis Pérez"));
        fachada.registrarUsuario(new Usuario("sofia", "Sofía Díaz"));
        fachada.registrarRecurso(TipoRecurso.LIBRO, "Clean Code", "Robert C. Martin");
        fachada.registrarRecurso(TipoRecurso.LIBRO, "Patrones de diseño", "Erich Gamma");
        fachada.registrarRecurso(TipoRecurso.LIBRO, "Refactoring", "Martin Fowler");
        fachada.registrarRecurso(TipoRecurso.EQUIPO, "Portátil ThinkPad", "Lenovo");
        fachada.registrarRecurso(TipoRecurso.EQUIPO, "Proyector", "Epson");
        fachada.registrarRecurso(TipoRecurso.OTRO, "Sala de estudio 1", "Espacio");
        fachada.registrarRecurso(TipoRecurso.OTRO, "Calculadora científica", "Calculadora");
    }
}
