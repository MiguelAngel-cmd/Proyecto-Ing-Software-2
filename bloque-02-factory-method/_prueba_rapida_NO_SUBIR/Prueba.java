import com.biblioteca.fabrica.FabricaRecurso;
import com.biblioteca.modelo.*;

public class Prueba {
    public static void main(String[] args) {
        for (TipoRecurso tipo : TipoRecurso.values()) {
            Recurso r = FabricaRecurso.para(tipo).crear(tipo.getPrefijo() + "-001", "Ejemplo", "dato");
            System.out.println(tipo + " -> " + r.getClass().getSimpleName() + ", plazo " + r.getRegla().plazoDias() + " días");
        }
        try {
            FabricaRecurso.para(TipoRecurso.LIBRO).crear("L-001", "", "Martin");
        } catch (Exception e) {
            System.out.println("Error esperado: " + e.getMessage());
        }
    }
}
