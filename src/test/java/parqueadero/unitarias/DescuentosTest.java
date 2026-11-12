package parqueadero.unitarias;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import parqueadero.Descuentos;

/**
 * Pruebas unitarias de {@link Descuentos#aplicarDescuento(int, String)}, construidas con TDD.
 *
 * <p>El historial de git conserva el ciclo: primero una prueba en rojo (falla porque el
 * comportamiento no existe), luego el código mínimo en verde y al final un refactor con
 * todas las pruebas pasando.
 *
 * <p>Elementos de JUnit 5 usados: {@code @Test} marca una prueba, {@code @DisplayName} da
 * un nombre legible, {@code @ParameterizedTest} con {@code @ValueSource} repite la prueba
 * con varios datos, {@code assertEquals(esperado, real)} compara resultados y
 * {@code assertThrows} exige una excepción y permite revisar su mensaje.
 */
class DescuentosTest {

    /** 3000 con 10 % de descuento queda en 2700. */
    @Test
    @DisplayName("Un cliente frecuente recibe diez por ciento de descuento")
    void clienteFrecuenteRecibeDiezPorCientoDeDescuento() {
        assertEquals(2700, Descuentos.aplicarDescuento(3000, "frecuente"));
    }

    /** 25000 con 20 % de descuento queda en 20000. */
    @Test
    @DisplayName("Un cliente con convenio recibe veinte por ciento de descuento")
    void clienteConConvenioRecibeVeintePorCientoDeDescuento() {
        assertEquals(20000, Descuentos.aplicarDescuento(25000, "convenio"));
    }

    /** El tipo {@code ninguno} paga el valor completo. */
    @Test
    @DisplayName("Un cliente sin tipo especial no recibe descuento")
    void clienteSinTipoEspecialNoRecibeDescuento() {
        assertEquals(3000, Descuentos.aplicarDescuento(3000, "ninguno"));
    }

    /** 1999 con 10 % da 1799,1; se descarta la fracción y queda 1799. */
    @Test
    @DisplayName("El descuento se redondea hacia abajo al peso entero")
    void descuentoSeRedondeaHaciaAbajoAlPesoEntero() {
        assertEquals(1799, Descuentos.aplicarDescuento(1999, "frecuente"));
    }

    /** Borde inferior válido: cero es un valor permitido y no cambia. */
    @ParameterizedTest(name = "cero con cliente {0} sigue siendo cero")
    @ValueSource(strings = {"ninguno", "frecuente", "convenio"})
    @DisplayName("Un valor de cero sigue siendo cero")
    void valorDeCeroSigueSiendoCero(String tipoCliente) {
        assertEquals(0, Descuentos.aplicarDescuento(0, tipoCliente));
    }

    /** Un valor menor que cero no tiene sentido y es un error. */
    @Test
    @DisplayName("Rechaza un valor negativo")
    void rechazaUnValorNegativo() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> Descuentos.aplicarDescuento(-1, "frecuente"));

        assertEquals("El valor no puede ser negativo", error.getMessage());
    }

    /** Solo se aceptan los tres tipos exactos; las variantes de mayúsculas fallan. */
    @ParameterizedTest(name = "el cliente \"{0}\" se rechaza")
    @ValueSource(strings = {"vip", "", "Frecuente", "CONVENIO"})
    @DisplayName("Rechaza un tipo de cliente desconocido")
    void rechazaUnTipoDeClienteDesconocido(String tipoCliente) {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> Descuentos.aplicarDescuento(3000, tipoCliente));

        assertEquals("Tipo de cliente no válido", error.getMessage());
    }
}
