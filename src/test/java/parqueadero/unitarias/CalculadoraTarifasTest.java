package parqueadero.unitarias;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import parqueadero.CalculadoraTarifas;

/** Pruebas unitarias de calcularTarifa: particiones de equivalencia y valores límite. */
class CalculadoraTarifasTest {

    private static final String MENSAJE_TIPO_INVALIDO = "Tipo de vehículo no válido";
    private static final String MENSAJE_MINUTOS_INVALIDOS = "Los minutos deben estar entre 0 y 1440";

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

    // --- Minutos de gracia: 15 minutos o menos no se paga nada ---

    @ParameterizedTest(name = "{1} con {0} minutos no paga")
    @CsvSource({
        "0, carro", "1, carro", "14, carro", "15, carro",
        "0, moto", "1, moto", "14, moto", "15, moto"
    })
    @DisplayName("Una estadía dentro de la gracia no se cobra")
    void estadiaDentroDeLaGraciaNoSeCobra(int minutos, String tipoVehiculo) {
        assertEquals(0, calculadora.calcularTarifa(minutos, tipoVehiculo));
    }

    @ParameterizedTest(name = "{0} con 16 minutos paga {1}")
    @CsvSource({"carro, 3000", "moto, 1500"})
    @DisplayName("El primer minuto después de la gracia cobra una hora")
    void primerMinutoDespuesDeLaGraciaCobraUnaHora(String tipoVehiculo, int esperado) {
        assertEquals(esperado, calculadora.calcularTarifa(16, tipoVehiculo));
    }

    // --- Cobro por hora o fracción: el cambio de hora ocurre al pasar el minuto 60 ---

    @ParameterizedTest(name = "carro con {0} minutos paga {1}")
    @CsvSource({"60, 3000", "61, 6000", "120, 6000", "121, 9000"})
    @DisplayName("Un carro paga cada hora o fracción de hora")
    void carroPagaCadaHoraOFraccion(int minutos, int esperado) {
        assertEquals(esperado, calculadora.calcularTarifa(minutos, "carro"));
    }

    @ParameterizedTest(name = "moto con {0} minutos paga {1}")
    @CsvSource({"60, 1500", "61, 3000", "120, 3000", "121, 4500"})
    @DisplayName("Una moto paga cada hora o fracción de hora")
    void motoPagaCadaHoraOFraccion(int minutos, int esperado) {
        assertEquals(esperado, calculadora.calcularTarifa(minutos, "moto"));
    }

    // --- Tope diario: carro 25000, moto 12000 ---

    @ParameterizedTest(name = "carro con {0} minutos paga {1}")
    @CsvSource({"480, 24000", "481, 25000", "540, 25000", "1440, 25000"})
    @DisplayName("Un carro nunca paga más del tope diario")
    void carroNuncaPagaMasDelTopeDiario(int minutos, int esperado) {
        assertEquals(esperado, calculadora.calcularTarifa(minutos, "carro"));
    }

    @ParameterizedTest(name = "moto con {0} minutos paga {1}")
    @CsvSource({"480, 12000", "481, 12000", "600, 12000", "1440, 12000"})
    @DisplayName("Una moto nunca paga más del tope diario")
    void motoNuncaPagaMasDelTopeDiario(int minutos, int esperado) {
        assertEquals(esperado, calculadora.calcularTarifa(minutos, "moto"));
    }

    // --- Rango válido de minutos: de 0 a 1440, ambos incluidos ---

    @ParameterizedTest(name = "{0} acepta 1440 minutos")
    @ValueSource(strings = {"carro", "moto"})
    @DisplayName("Acepta el límite superior de 1440 minutos")
    void aceptaElLimiteSuperiorDe1440Minutos(String tipoVehiculo) {
        assertTrue(calculadora.calcularTarifa(1440, tipoVehiculo) > 0);
    }

    @ParameterizedTest(name = "{1} con {0} minutos se rechaza")
    @CsvSource({
        "-1, carro", "-100, carro", "1441, carro", "5000, carro",
        "-1, moto", "-100, moto", "1441, moto", "5000, moto"
    })
    @DisplayName("Rechaza minutos fuera del rango")
    void rechazaMinutosFueraDelRango(int minutos, String tipoVehiculo) {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> calculadora.calcularTarifa(minutos, tipoVehiculo));

        assertEquals(MENSAJE_MINUTOS_INVALIDOS, error.getMessage());
    }

    // --- Tipos de vehículo inválidos ---

    @ParameterizedTest(name = "el tipo \"{0}\" se rechaza")
    @ValueSource(strings = {"bicicleta", "camion", "", "Carro", "MOTO", " carro"})
    @DisplayName("Rechaza tipos de vehículo no permitidos")
    void rechazaTiposDeVehiculoNoPermitidos(String tipoVehiculo) {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> calculadora.calcularTarifa(30, tipoVehiculo));

        assertEquals(MENSAJE_TIPO_INVALIDO, error.getMessage());
    }

    @Test
    @DisplayName("Un tipo inválido se reporta antes que unos minutos inválidos")
    void tipoInvalidoSeReportaAntesQueMinutosInvalidos() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> calculadora.calcularTarifa(-5, "bicicleta"));

        assertEquals(MENSAJE_TIPO_INVALIDO, error.getMessage());
    }
}
