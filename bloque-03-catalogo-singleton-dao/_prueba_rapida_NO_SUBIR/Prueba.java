import com.biblioteca.catalogo.Catalogo;
import com.biblioteca.modelo.TipoRecurso;

public class Prueba {
    public static void main(String[] args) {
        Catalogo a = Catalogo.getInstancia();
        Catalogo b = Catalogo.getInstancia();
        System.out.println("¿Es la misma instancia? " + (a == b));
        a.registrar(TipoRecurso.LIBRO, "Clean Code", "Martin");
        a.registrar(TipoRecurso.EQUIPO, "Proyector", "Epson");
        b.listar().forEach(r -> System.out.println(r.getId() + " - " + r.getTitulo()));   // se ve desde "b"
    }
}
