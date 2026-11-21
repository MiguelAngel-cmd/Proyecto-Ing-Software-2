import com.biblioteca.espera.ListaEspera;
import com.biblioteca.modelo.*;

public class Prueba {
    public static void main(String[] args) {
        Recurso libro = new Libro("L-001", "Clean Code", "Martin");
        Usuario ana = new Usuario("ana", "Ana");
        Usuario luis = new Usuario("luis", "Luis");
        ListaEspera lista = new ListaEspera(libro);
        lista.suscribir(ana);
        lista.suscribir(luis);
        System.out.println("Se avisó a: " + lista.notificarSiguiente());     // solo al primero
        System.out.println("Avisos de Ana: " + ana.getAvisos());
        System.out.println("Avisos de Luis: " + luis.getAvisos());
    }
}
