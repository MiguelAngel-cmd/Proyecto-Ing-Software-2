package com.biblioteca;

import com.biblioteca.espera.*;
import com.biblioteca.modelo.*;

public class Prueba {
    public static void main(String[] args) {
        Recurso libro = new Libro("L-001", "Clean Code", "Martin");
        Usuario luis = new Usuario("luis", "Luis");
        Usuario sofia = new Usuario("sofia", "Sofía");
        ListaEspera lista = new ListaEspera(libro);
        lista.suscribir(new AvisoUsuario(luis));
        lista.suscribir(new AvisoUsuario(sofia));
        System.out.println("En la fila: " + lista.idsEnEspera());
        System.out.println("El libro se devolvió. Se avisó a: " + lista.notificarSiguiente());
        System.out.println("Avisos de Luis:  " + luis.getAvisos());
        System.out.println("Avisos de Sofía: " + sofia.getAvisos());
        System.out.println("Sigue en la fila: " + lista.idsEnEspera());
    }
}
