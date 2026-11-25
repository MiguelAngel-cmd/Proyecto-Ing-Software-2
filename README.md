# Biblioteca y préstamo de recursos

Parcial de Patrones de Diseño (Java 21) — **Propuesta 5**. Control de préstamos de libros, equipos y otros
recursos, con consola y una interfaz gráfica sencilla (Swing) que usan **el mismo núcleo**. Solo POO y patrones:
no usa librerías externas (JUnit solo para las pruebas).

**Autores:** Miguel Angel Barinas Vanegas · Bryan Steven Beltran Barbosa

## Cómo compilar y ejecutar

```bash
./compilar_y_ejecutar.sh          # consola   (Windows: compilar_y_ejecutar.bat)
./compilar_y_ejecutar.sh gui      # ventana

mvn test                          # pruebas unitarias (con Maven)
```

> En Windows, si se ven mal las tildes, ejecute antes `chcp 65001`.

Los datos viven en memoria y se cargan al iniciar (`CargaInicial`): usuarios `ana`, `luis`, `sofia`;
libros `L-001` a `L-003`, equipos `E-001`, `E-002` y otros `O-001`, `O-002`.

## Funcionalidades

| Requisito | Qué hace |
|---|---|
| RF-01 | Registrar, consultar, modificar y dar de baja recursos de tres tipos (libro, equipo, otro) en un catálogo único |
| RF-02 | Prestar un recurso validando disponibilidad y las reglas de su tipo (plazo y máximo por usuario) |
| RF-03 | Devolver un recurso y actualizar su disponibilidad |
| RF-04 | Renovar un préstamo respetando el máximo de renovaciones del tipo |
| RF-05 | Calcular la multa por retraso con la política vigente (tres políticas intercambiables) |
| RF-06 | Lista de espera: al devolverse un recurso se avisa al primero de la fila |

| Tipo | Plazo | Renovaciones | Máx. por usuario |
|---|---|---|---|
| Libro | 14 días | 2 | 3 |
| Equipo | 3 días | 1 | 1 |
| Otro | 7 días | 1 | 2 |

Reglas adicionales: no se renueva un préstamo vencido ni uno que tenga personas esperando. La fecha del sistema se
puede adelantar desde el menú para demostrar multas sin esperar días reales.

## Patrones de diseño

| Problema | Patrón | Categoría | Clases |
|---|---|---|---|
| Crear recursos de distintos tipos con sus reglas | Factory Method | Creacional | `FabricaRecurso`, `FabricaLibro`, `FabricaEquipo`, `FabricaOtro` |
| Catálogo único y consistente | Singleton | Creacional | `Catalogo` |
| Políticas de multa intercambiables | Strategy | Comportamiento | `PoliticaMulta`, `MultaPorDia`, `MultaProgresiva`, `MultaPorTipoRecurso` |
| Aviso al siguiente de la lista de espera | Observer | Comportamiento | `ListaEspera`, `ObservadorDisponibilidad`, `Usuario` |
| Consola y ventana sobre el mismo núcleo | Facade | Estructural | `BibliotecaFachada` |

Además, `RecursoDAO` (y su versión en memoria) separa el acceso a los datos del catálogo.

## Estructura

```
com.biblioteca
  app/        Main, CargaInicial
  consola/    MenuConsola                 ┐ interfaces: solo llaman a la fachada
  vista/      VentanaPrincipal            ┘
  servicio/   BibliotecaFachada, PrestamoServicio
  catalogo/   Catalogo (Singleton)   dao/  RecursoDAO, memoria/RecursoDAOMemoria
  fabrica/    FabricaRecurso y fábricas concretas
  multas/     PoliticaMulta y políticas
  espera/     ListaEspera, ObservadorDisponibilidad
  modelo/     Recurso, Libro, Equipo, Otro, TipoRecurso, ReglaPrestamo, Usuario, Prestamo
  util/       Reloj          excepcion/  BibliotecaException
```

## Demostración rápida (consola)

1. Opción 5: prestar `L-001` a `ana` (vence en 14 días).
2. Opción 9: `luis` se une a la lista de espera de `L-001`.
3. Opción 12: adelantar 20 días. Opción 6: devolver el préstamo → multa y aviso a Luis.
4. Opción 10 (avisos de `luis`) y opción 11 (cambiar la política de multas).

## Cómo extender (cambios de requisito)

| Cambio | Qué se agrega |
|---|---|
| Nueva política de multa | Una clase que implemente `PoliticaMulta` y una línea en `PoliticaMulta.TODAS` |
| Nuevo tipo de recurso | Subclase de `Recurso`, su fábrica y un caso en `FabricaRecurso.para` y `TipoRecurso` |
| Otro canal de aviso | Otra clase que implemente `ObservadorDisponibilidad` |
| Otra interfaz | Una clase nueva que use `BibliotecaFachada` |

## Declaración de uso de IA

Se usó una herramienta de IA como apoyo para escribir y simplificar el código y la documentación. El grupo revisó
el resultado y puede explicar cada clase.
