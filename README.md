# Parqueadero EstacionaYa · Java

Segundo parcial de Pruebas de Software (Unidad 2: pruebas ágiles y DevOps). Sistema que calcula lo que paga cada vehículo de un parqueadero y aplica descuentos a los clientes.

## Requisitos

| Herramienta | Versión probada | Para qué se usa |
|---|---|---|
| Java (JDK) | 21 (Amazon Corretto 21.0.12) | Compilar y ejecutar |
| [Maven](https://maven.apache.org/download.cgi) | 3.9.16 (mínimo 3.9) | Construcción, pruebas, análisis y cobertura |

El `pom.xml` usa el parent de Spring Boot 4.1.1 solo para heredar versiones compatibles de JUnit y de los plugins de Maven; el proyecto no usa Spring. La primera ejecución descarga las dependencias y tarda unos minutos.

Comprueba tus versiones con `java -version` y `mvn -version`. Si tienes varios JDK instalados (por ejemplo con SDKMAN), asegúrate de que `JAVA_HOME` apunte al 21.

## Cómo correr todo

Ejecuta los comandos desde la raíz del repositorio.

```bash
mvn test -Dtest='**/unitarias/*Test'   # pruebas unitarias
mvn test -Dtest=RunCucumberTest        # escenarios BDD
mvn checkstyle:check                   # análisis estático
mvn verify                             # todo: pruebas, reporte y mínimo de cobertura de 90 %
```

En zsh deja las comillas simples en el patrón `**/unitarias/*Test`; sin ellas el shell intenta expandirlo.

El reporte de cobertura queda en `target/site/jacoco/index.html`.

## Estructura

```text
.
├── .github/workflows/ci-cd.yml        # pipeline de CI/CD (lo entrega el docente, no se modifica)
├── config/checkstyle.xml              # reglas del análisis estático
├── src/main/java/parqueadero/
│   ├── CalculadoraTarifas.java        # calcularTarifa(minutos, tipoVehiculo)
│   ├── Descuentos.java                # aplicarDescuento(valor, tipoCliente)
│   ├── Registro.java                  # una estadía: tipo y minutos
│   └── Reportes.java                  # resumen(registros)
├── src/test/java/parqueadero/
│   ├── unitarias/                     # pruebas unitarias (JUnit 5)
│   └── bdd/                           # RunCucumberTest y TarifasSteps (Cucumber)
├── src/test/resources/features/tarifas.feature   # escenarios en español
└── pom.xml                            # dependencias, plugins y reglas de cobertura
```

## Reglas de negocio

- **Vehículos:** solo `carro` y `moto`.
- **Duración:** entero entre 0 y 1440 minutos.
- **Gracia:** hasta 15 minutos no se paga.
- **Cobro:** cada hora o fracción; carro 3000 y moto 1500 pesos por hora.
- **Tope diario:** carro 25000 y moto 12000 pesos.
- **Descuentos:** `ninguno` 0 %, `frecuente` 10 %, `convenio` 20 %, redondeado hacia abajo.

## Cómo funcionan `verify` y la cobertura

Maven ejecuta fases en orden fijo: `validate`, `compile`, `test`, `package`, `verify`, `install`, `deploy`. Al pedir una fase se ejecutan también las anteriores, y los plugins hacen el trabajo real enlazando sus *goals* a una fase.

1. `jacoco:prepare-agent` calcula la opción de JVM `-javaagent` y la guarda en la propiedad `argLine`.
2. En la fase `test`, Surefire lanza una JVM con esa `argLine`; el agente de JaCoCo inserta sondas en el bytecode y registra qué líneas y ramas se ejecutan.
3. Al terminar, el agente escribe `target/jacoco.exec`.
4. `jacoco:report` convierte ese archivo en `target/site/jacoco/index.html`.
5. `jacoco:check`, en la fase `verify`, exige al menos 90 % de líneas y 90 % de ramas cubiertas. Si no se cumple, el build falla.

Por eso `mvn verify` valida la cobertura y `mvn test` no. Los comentarios de `pom.xml` explican cada paso con más detalle.

## Pipeline de CI/CD

El archivo `.github/workflows/ci-cd.yml` corre en cada push a `main` con cuatro jobs independientes: análisis estático (`checkstyle:check`), pruebas unitarias, pruebas BDD y cobertura (`mvn verify`). Si los cuatro pasan, un quinto job simula el despliegue e imprime `Desplegado`.

## Historial de commits

Los commits conservan el ciclo de TDD de los descuentos: una prueba en rojo, el código mínimo en verde, y un refactor final con todas las pruebas pasando.
