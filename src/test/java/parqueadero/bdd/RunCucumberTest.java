package parqueadero.bdd;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

/**
 * Ejecuta con Cucumber los escenarios de src/test/resources/features.
 * Los pasos se buscan en el paquete parqueadero.bdd.
 *
 * <p>La clase no tiene código: toda su función está en las anotaciones de JUnit Platform.
 * Cuando Surefire ejecuta las pruebas, JUnit descubre esta suite y delega en Cucumber:
 * <ul>
 *   <li>{@code @Suite}: declara esta clase como una suite de JUnit Platform.</li>
 *   <li>{@code @IncludeEngines("cucumber")}: usa solo el motor de Cucumber en la suite.</li>
 *   <li>{@code @SelectClasspathResource("features")}: dónde buscar archivos {@code .feature}
 *       (carpeta {@code features} del classpath, que sale de {@code src/test/resources}).</li>
 *   <li>{@code @ConfigurationParameter} con {@code GLUE_PROPERTY_NAME}: paquete donde Cucumber
 *       busca las clases con los pasos (glue code), aquí {@code parqueadero.bdd}.</li>
 * </ul>
 * Se ejecuta con {@code mvn test -Dtest=RunCucumberTest}.
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "parqueadero.bdd")
public class RunCucumberTest {
}
