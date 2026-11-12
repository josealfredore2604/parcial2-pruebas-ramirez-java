package parqueadero.bdd;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;

import parqueadero.CalculadoraTarifas;
import parqueadero.Descuentos;

/**
 * Pasos (step definitions) de los escenarios BDD de tarifas y descuentos.
 *
 * <p>Conecta el archivo {@code features/tarifas.feature}, escrito en lenguaje de negocio,
 * con el código real del parqueadero. Con Cucumber:
 * <ul>
 *   <li>{@code @Dado} implementa los pasos "Dado" (preparan los datos).</li>
 *   <li>{@code @Cuando} implementa los pasos "Cuando" (ejecutan la acción).</li>
 *   <li>{@code @Entonces} implementa los pasos "Entonces" (verifican con {@code assertEquals}).</li>
 *   <li>El texto de cada anotación es una expresión de Cucumber: {@code {int}} captura un
 *       entero, {@code {word}} una palabra sin espacios y {@code {string}} un texto entre
 *       comillas. Los valores capturados llegan como parámetros del método.</li>
 * </ul>
 *
 * <p>Cucumber crea una instancia nueva de esta clase por cada escenario, así que los campos
 * funcionan como memoria compartida entre pasos sin mezclarse entre escenarios.
 */
public class TarifasSteps {

    private final CalculadoraTarifas calculadora = new CalculadoraTarifas();

    /** Tipo de vehículo de la estadía en curso. */
    private String vehiculo;

    /** Minutos de la estadía en curso. */
    private int minutos;

    /** Valor sobre el que se aplicará un descuento. */
    private int valor;

    /** Resultado del último cálculo (tarifa o valor con descuento). */
    private int resultado;

    /** Mensaje de la excepción de negocio, si el último intento fue rechazado. */
    private String error;

    /**
     * Registra el tipo de vehículo y los minutos de la estadía.
     *
     * @param vehiculo tipo de vehículo capturado del texto del paso
     * @param minutos minutos capturados del texto del paso
     */
    @Dado("un {word} que permaneció {int} minutos en el parqueadero")
    public void unaEstadia(String vehiculo, int minutos) {
        this.vehiculo = vehiculo;
        this.minutos = minutos;
    }

    /**
     * Registra el valor sobre el que se aplicará un descuento.
     *
     * @param valor valor en pesos capturado del texto del paso
     */
    @Dado("un valor a pagar de {int} pesos")
    public void unValorAPagar(int valor) {
        this.valor = valor;
    }

    /** Calcula la tarifa y guarda el resultado; un error haría fallar el escenario. */
    @Cuando("se calcula el valor a pagar")
    public void seCalculaElValorAPagar() {
        resultado = calculadora.calcularTarifa(minutos, vehiculo);
    }

    /** Calcula la tarifa y, si falla por una regla de negocio, guarda el mensaje. */
    @Cuando("se intenta calcular el valor a pagar")
    public void seIntentaCalcularElValorAPagar() {
        try {
            resultado = calculadora.calcularTarifa(minutos, vehiculo);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
    }

    /**
     * Aplica el descuento del tipo de cliente y guarda el resultado.
     *
     * @param tipoCliente tipo de cliente capturado del texto del paso
     */
    @Cuando("se aplica el descuento de un cliente {word}")
    public void seAplicaElDescuento(String tipoCliente) {
        resultado = Descuentos.aplicarDescuento(valor, tipoCliente);
    }

    /**
     * Aplica el descuento y, si falla por una regla de negocio, guarda el mensaje.
     *
     * @param tipoCliente tipo de cliente capturado del texto del paso
     */
    @Cuando("se intenta aplicar el descuento de un cliente {word}")
    public void seIntentaAplicarElDescuento(String tipoCliente) {
        try {
            resultado = Descuentos.aplicarDescuento(valor, tipoCliente);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
    }

    /**
     * Comprueba que la tarifa calculada sea la esperada.
     *
     * @param esperado valor en pesos que debe haberse calculado
     */
    @Entonces("el valor a pagar es {int} pesos")
    public void elValorAPagarEs(int esperado) {
        assertEquals(esperado, resultado);
    }

    /**
     * Comprueba que el valor con descuento sea el esperado.
     *
     * @param esperado valor en pesos que debe pagar el cliente
     */
    @Entonces("el cliente paga {int} pesos")
    public void elClientePaga(int esperado) {
        assertEquals(esperado, resultado);
    }

    /**
     * Comprueba que el sistema haya rechazado la operación con el mensaje esperado.
     *
     * @param mensaje texto exacto del error esperado
     */
    @Entonces("el sistema responde {string}")
    public void elSistemaResponde(String mensaje) {
        assertEquals(mensaje, error);
    }
}
