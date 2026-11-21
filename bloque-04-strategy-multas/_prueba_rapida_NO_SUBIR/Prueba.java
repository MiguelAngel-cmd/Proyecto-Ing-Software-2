import com.biblioteca.modelo.*;
import com.biblioteca.multas.PoliticaMulta;

public class Prueba {
    public static void main(String[] args) {
        Recurso libro = new Libro("L-001", "Clean Code", "Martin");
        Recurso equipo = new Equipo("E-001", "Portátil", "Lenovo");
        for (PoliticaMulta p : PoliticaMulta.TODAS) {        // misma interfaz, resultados distintos
            System.out.println(p.getNombre());
            System.out.println("   5 días, libro: " + p.calcular(5, libro) + " | 5 días, equipo: " + p.calcular(5, equipo));
        }
    }
}
