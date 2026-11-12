package parqueadero.unitarias;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import parqueadero.CalculadoraTarifas;

/** Pruebas unitarias iniciales de calcularTarifa. Están incompletas a propósito. */
class CalculadoraTarifasTest {

    private final CalculadoraTarifas calculadora = new CalculadoraTarifas();

    @Test
    @DisplayName("Un carro con una hora y un minuto paga dos horas")
    void carroConUnaHoraYUnMinutoPagaDosHoras() {
        assertEquals(6000, calculadora.calcularTarifa(61, "carro"));
    }

    @Test
    @DisplayName("Una moto con media hora paga una hora")
    void motoConMediaHoraPagaUnaHora() {
        assertEquals(1500, calculadora.calcularTarifa(30, "moto"));
    }

    @Test
    @DisplayName("Rechaza un tipo de vehículo desconocido")
    void rechazaUnTipoDeVehiculoDesconocido() {
        assertThrows(IllegalArgumentException.class, () -> calculadora.calcularTarifa(30, "bicicleta"));
    }
}
