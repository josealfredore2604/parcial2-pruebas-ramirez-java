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

/**
 * Pruebas unitarias de {@link CalculadoraTarifas#calcularTarifa(int, String)}.
 *
 * <p>Cada regla de negocio se prueba con sus particiones de equivalencia (grupos de
 * entradas que el sistema trata igual) y con sus valores límite (los bordes de cada
 * grupo). Elementos de JUnit 5 usados en esta clase:
 * <ul>
 *   <li>{@code @Test}: marca un método como prueba.</li>
 *   <li>{@code @DisplayName}: nombre legible que aparece en los reportes.</li>
 *   <li>{@code @ParameterizedTest}: ejecuta el mismo método con varios datos;
 *       el atributo {@code name} define el título de cada ejecución.</li>
 *   <li>{@code @CsvSource}: datos de entrada; cada texto es una fila y las comas separan
 *       los argumentos del método.</li>
 *   <li>{@code @ValueSource}: lista de un solo argumento.</li>
 *   <li>{@code assertEquals(esperado, real)}: falla si los valores difieren.</li>
 *   <li>{@code assertThrows}: exige que el código lance la excepción indicada y la devuelve
 *       para poder revisar su mensaje.</li>
 *   <li>{@code assertTrue}: falla si la condición es falsa.</li>
 * </ul>
 */
class CalculadoraTarifasTest {

    private static final String MENSAJE_TIPO_INVALIDO = "Tipo de vehículo no válido";
    private static final String MENSAJE_MINUTOS_INVALIDOS = "Los minutos deben estar entre 0 y 1440";

    /** Objeto bajo prueba; es inmutable, así que se comparte entre todas las pruebas. */
    private final CalculadoraTarifas calculadora = new CalculadoraTarifas();

    /** Una fracción de hora se cobra como hora completa. */
    @Test
    @DisplayName("Un carro con una hora y un minuto paga dos horas")
    void carroConUnaHoraYUnMinutoPagaDosHoras() {
        assertEquals(6000, calculadora.calcularTarifa(61, "carro"));
    }

    /** Una estadía corta, pasada la gracia, cobra una hora de moto. */
    @Test
    @DisplayName("Una moto con media hora paga una hora")
    void motoConMediaHoraPagaUnaHora() {
        assertEquals(1500, calculadora.calcularTarifa(30, "moto"));
    }

    /** Un tipo distinto de carro y moto es un error. */
    @Test
    @DisplayName("Rechaza un tipo de vehículo desconocido")
    void rechazaUnTipoDeVehiculoDesconocido() {
        assertThrows(IllegalArgumentException.class, () -> calculadora.calcularTarifa(30, "bicicleta"));
    }

    // --- Minutos de gracia: 15 minutos o menos no se paga nada ---

    /** Límites de la gracia: 0 y 15 son los extremos; 1 y 14 son valores internos. */
    @ParameterizedTest(name = "{1} con {0} minutos no paga")
    @CsvSource({
        "0, carro", "1, carro", "14, carro", "15, carro",
        "0, moto", "1, moto", "14, moto", "15, moto"
    })
    @DisplayName("Una estadía dentro de la gracia no se cobra")
    void estadiaDentroDeLaGraciaNoSeCobra(int minutos, String tipoVehiculo) {
        assertEquals(0, calculadora.calcularTarifa(minutos, tipoVehiculo));
    }

    /** El minuto 16 es el primero que se cobra: una hora completa. */
    @ParameterizedTest(name = "{0} con 16 minutos paga {1}")
    @CsvSource({"carro, 3000", "moto, 1500"})
    @DisplayName("El primer minuto después de la gracia cobra una hora")
    void primerMinutoDespuesDeLaGraciaCobraUnaHora(String tipoVehiculo, int esperado) {
        assertEquals(esperado, calculadora.calcularTarifa(16, tipoVehiculo));
    }

    // --- Cobro por hora o fracción: el cambio de hora ocurre al pasar el minuto 60 ---

    /** Cada múltiplo de 60 es el borde: un minuto más suma una hora. */
    @ParameterizedTest(name = "carro con {0} minutos paga {1}")
    @CsvSource({"60, 3000", "61, 6000", "120, 6000", "121, 9000"})
    @DisplayName("Un carro paga cada hora o fracción de hora")
    void carroPagaCadaHoraOFraccion(int minutos, int esperado) {
        assertEquals(esperado, calculadora.calcularTarifa(minutos, "carro"));
    }

    /** Igual que el carro, con la tarifa por hora de la moto. */
    @ParameterizedTest(name = "moto con {0} minutos paga {1}")
    @CsvSource({"60, 1500", "61, 3000", "120, 3000", "121, 4500"})
    @DisplayName("Una moto paga cada hora o fracción de hora")
    void motoPagaCadaHoraOFraccion(int minutos, int esperado) {
        assertEquals(esperado, calculadora.calcularTarifa(minutos, "moto"));
    }

    // --- Tope diario: carro 25000, moto 12000 ---

    /** 480 minutos (8 h) aún no llegan al tope; desde 481 el valor se limita a 25000. */
    @ParameterizedTest(name = "carro con {0} minutos paga {1}")
    @CsvSource({"480, 24000", "481, 25000", "540, 25000", "1440, 25000"})
    @DisplayName("Un carro nunca paga más del tope diario")
    void carroNuncaPagaMasDelTopeDiario(int minutos, int esperado) {
        assertEquals(esperado, calculadora.calcularTarifa(minutos, "carro"));
    }

    /** La moto llega al tope justo a las 8 h (480 minutos) y no lo supera. */
    @ParameterizedTest(name = "moto con {0} minutos paga {1}")
    @CsvSource({"480, 12000", "481, 12000", "600, 12000", "1440, 12000"})
    @DisplayName("Una moto nunca paga más del tope diario")
    void motoNuncaPagaMasDelTopeDiario(int minutos, int esperado) {
        assertEquals(esperado, calculadora.calcularTarifa(minutos, "moto"));
    }

    // --- Rango válido de minutos: de 0 a 1440, ambos incluidos ---

    /** 1440 minutos (24 h) es la duración máxima válida y se cobra. */
    @ParameterizedTest(name = "{0} acepta 1440 minutos")
    @ValueSource(strings = {"carro", "moto"})
    @DisplayName("Acepta el límite superior de 1440 minutos")
    void aceptaElLimiteSuperiorDe1440Minutos(String tipoVehiculo) {
        assertTrue(calculadora.calcularTarifa(1440, tipoVehiculo) > 0);
    }

    /** -1 y 1441 son los valores justo fuera del rango; los otros son casos lejanos. */
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

    /** Cubre tipos desconocidos, vacío y variantes de mayúsculas o con espacios. */
    @ParameterizedTest(name = "el tipo \"{0}\" se rechaza")
    @ValueSource(strings = {"bicicleta", "camion", "", "Carro", "MOTO", " carro"})
    @DisplayName("Rechaza tipos de vehículo no permitidos")
    void rechazaTiposDeVehiculoNoPermitidos(String tipoVehiculo) {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> calculadora.calcularTarifa(30, tipoVehiculo));

        assertEquals(MENSAJE_TIPO_INVALIDO, error.getMessage());
    }

    /** Si fallan ambas validaciones, el error que se informa es el del tipo. */
    @Test
    @DisplayName("Un tipo inválido se reporta antes que unos minutos inválidos")
    void tipoInvalidoSeReportaAntesQueMinutosInvalidos() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> calculadora.calcularTarifa(-5, "bicicleta"));

        assertEquals(MENSAJE_TIPO_INVALIDO, error.getMessage());
    }
}
