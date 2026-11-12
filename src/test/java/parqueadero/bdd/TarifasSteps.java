package parqueadero.bdd;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;

import parqueadero.CalculadoraTarifas;
import parqueadero.Descuentos;

/** Pasos de los escenarios BDD de tarifas y descuentos. */
public class TarifasSteps {

    private final CalculadoraTarifas calculadora = new CalculadoraTarifas();

    private String vehiculo;
    private int minutos;
    private int valor;
    private int resultado;
    private String error;

    @Dado("un {word} que permaneció {int} minutos en el parqueadero")
    public void unaEstadia(String vehiculo, int minutos) {
        this.vehiculo = vehiculo;
        this.minutos = minutos;
    }

    @Dado("un valor a pagar de {int} pesos")
    public void unValorAPagar(int valor) {
        this.valor = valor;
    }

    @Cuando("se calcula el valor a pagar")
    public void seCalculaElValorAPagar() {
        resultado = calculadora.calcularTarifa(minutos, vehiculo);
    }

    @Cuando("se intenta calcular el valor a pagar")
    public void seIntentaCalcularElValorAPagar() {
        try {
            resultado = calculadora.calcularTarifa(minutos, vehiculo);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
    }

    @Cuando("se aplica el descuento de un cliente {word}")
    public void seAplicaElDescuento(String tipoCliente) {
        resultado = Descuentos.aplicarDescuento(valor, tipoCliente);
    }

    @Cuando("se intenta aplicar el descuento de un cliente {word}")
    public void seIntentaAplicarElDescuento(String tipoCliente) {
        try {
            resultado = Descuentos.aplicarDescuento(valor, tipoCliente);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
    }

    @Entonces("el valor a pagar es {int} pesos")
    public void elValorAPagarEs(int esperado) {
        assertEquals(esperado, resultado);
    }

    @Entonces("el cliente paga {int} pesos")
    public void elClientePaga(int esperado) {
        assertEquals(esperado, resultado);
    }

    @Entonces("el sistema responde {string}")
    public void elSistemaResponde(String mensaje) {
        assertEquals(mensaje, error);
    }
}
