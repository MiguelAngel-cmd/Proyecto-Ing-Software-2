package com.biblioteca.fabrica;

import com.biblioteca.excepcion.BibliotecaException;
import com.biblioteca.modelo.Recurso;
import com.biblioteca.modelo.TipoRecurso;

/**
 * PATRÓN FACTORY METHOD (creacional) — rol: Creator.
 *
 * {@link #crear(DatosRecurso)} es el método plantilla: valida los datos y delega
 * en {@link #crearRecurso(DatosRecurso)}, el factory method, que cada fábrica
 * concreta implementa para construir "su" tipo de recurso. Para agregar un tipo
 * nuevo solo se crea una fábrica nueva; el código que usa fábricas no cambia.
 */
public abstract class FabricaRecurso {

    public final Recurso crear(DatosRecurso datos) {
        validar(datos);
        return crearRecurso(datos);
    }

    /** Factory method: lo implementa cada fábrica concreta. */
    protected abstract Recurso crearRecurso(DatosRecurso datos);

    public abstract TipoRecurso getTipo();

    private void validar(DatosRecurso datos) {
        if (datos == null || vacio(datos.getTitulo())) {
            throw new BibliotecaException("El título del recurso es obligatorio.");
        }
        if (vacio(datos.getDetalle())) {
            throw new BibliotecaException("El dato '" + etiquetaPorTipo() + "' es obligatorio.");
        }
    }

    private String etiquetaPorTipo() {
        return switch (getTipo()) {
            case LIBRO -> "Autor";
            case EQUIPO -> "Marca";
            case OTRO -> "Categoría";
        };
    }

    private boolean vacio(String texto) {
        return texto == null || texto.isBlank();
    }
}
