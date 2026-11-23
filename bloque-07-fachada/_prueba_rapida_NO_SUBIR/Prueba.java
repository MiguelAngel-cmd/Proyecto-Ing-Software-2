import com.biblioteca.catalogo.Catalogo;
import com.biblioteca.modelo.*;
import com.biblioteca.servicio.BibliotecaFachada;

public class Prueba {
    public static void main(String[] args) {
        Catalogo.getInstancia().vaciar();
        BibliotecaFachada fachada = new BibliotecaFachada();       // la puerta única del sistema
        fachada.registrarUsuario(new Usuario("ana", "Ana"));
        fachada.registrarRecurso(TipoRecurso.EQUIPO, "Proyector", "Epson");
        Prestamo p = fachada.prestar("E-001", "ana");
        System.out.println("Vence: " + p.getFechaVencimiento() + " | activos: " + fachada.prestamosActivos().size());
        fachada.avanzarDias(5);
        System.out.println("Multa por devolver tarde: " + fachada.devolver(p.getId()).getMulta());
    }
}
