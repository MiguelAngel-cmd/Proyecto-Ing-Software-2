import com.biblioteca.catalogo.Catalogo;
import com.biblioteca.modelo.*;
import com.biblioteca.servicio.PrestamoServicio;
import com.biblioteca.util.Reloj;

public class Prueba {
    public static void main(String[] args) {
        Reloj reloj = new Reloj();
        Catalogo catalogo = Catalogo.getInstancia();
        catalogo.vaciar();
        catalogo.registrar(TipoRecurso.LIBRO, "Clean Code", "Martin");
        PrestamoServicio servicio = new PrestamoServicio(catalogo, reloj);
        Usuario ana = new Usuario("ana", "Ana");
        Usuario luis = new Usuario("luis", "Luis");

        Prestamo p = servicio.prestar("L-001", ana);
        System.out.println("Préstamo #" + p.getId() + " vence " + p.getFechaVencimiento());
        servicio.unirseListaEspera("L-001", luis);
        reloj.avanzarDias(20);                                   // pasan 20 días: 6 de retraso
        Prestamo devuelto = servicio.devolver(p.getId());
        System.out.println("Multa: " + devuelto.getMulta());
        System.out.println("Avisos de Luis: " + luis.getAvisos());
    }
}
