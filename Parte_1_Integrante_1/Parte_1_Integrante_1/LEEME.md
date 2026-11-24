# Parte 1 — Integrante 1

**Qué contiene** (23 archivos del sistema + 5 clases de pruebas): el modelo del dominio y **cuatro patrones**.

| Paquete | Contenido | Patrón |
|---|---|---|
| `modelo`, `excepcion` | Recurso, Libro, Equipo, Otro, TipoRecurso, ReglaPrestamo, Usuario, Prestamo, BibliotecaException | — |
| `fabrica` | FabricaRecurso, FabricaLibro, FabricaEquipo, FabricaOtro | Factory Method |
| `catalogo`, `dao` | Catalogo, RecursoDAO, RecursoDAOMemoria | Singleton (+ DAO) |
| `multas` | PoliticaMulta, MultaPorDia, MultaProgresiva, MultaPorTipoRecurso | Strategy |
| `espera` | ListaEspera, ObservadorDisponibilidad | Observer |
| `util` | Reloj | — |
| raíz | `pom.xml`, `.gitignore`, `README.md` básico | — |

Esta parte **compila sola** y trae 19 pruebas unitarias (todavía no se puede ejecutar el programa: eso llega con la Parte 2).

## Pasos (los hace el Integrante 1, **primero**)

```bash
git clone https://github.com/MiguelAngel-cmd/Proyecto-Ing-Software-2.git      # o git pull si ya lo tiene
cd Proyecto-Ing-Software-2
git rm -r -q bloque-*                 # quita las carpetas por bloques de la versión anterior
```

Después copie **todo el contenido de la carpeta `contenido`** de este zip dentro de la carpeta del repositorio
(deje `pom.xml`, `src`, `.gitignore` y `README.md` en la raíz; acepte reemplazar). Luego:

```bash
git add -A
git commit -m "feat: modelo del dominio y patrones Factory Method, Singleton, Strategy y Observer con sus pruebas"
git push
```

En Linux/macOS la copia es: `cp -R ../Parte_1_Integrante_1/contenido/. .`

## Para comprobar

Con Maven: `mvn test` (deben pasar 19 pruebas). Sin Maven: `javac -encoding UTF-8 -d out $(find src/main -name "*.java")`.
