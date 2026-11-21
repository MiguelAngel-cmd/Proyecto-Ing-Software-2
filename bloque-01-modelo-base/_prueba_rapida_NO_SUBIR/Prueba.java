import com.biblioteca.modelo.*;

public class Prueba {
    public static void main(String[] args) {
        Recurso libro = new Libro("L-001", "Clean Code", "Martin");
        Recurso equipo = new Equipo("E-001", "Portátil", "Lenovo");
        System.out.println("Libro: " + libro.getRegla());      // cada tipo trae su regla
        System.out.println("Equipo: " + equipo.getRegla());
        libro.marcarPrestado();
        System.out.println("Estado del libro tras prestarlo: " + libro.getEstado());
    }
}
