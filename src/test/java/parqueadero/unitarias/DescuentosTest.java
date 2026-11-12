package parqueadero.unitarias;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import parqueadero.Descuentos;

/** Pruebas unitarias de aplicarDescuento, construidas con TDD. */
class DescuentosTest {

    @Test
    @DisplayName("Un cliente frecuente recibe diez por ciento de descuento")
    void clienteFrecuenteRecibeDiezPorCientoDeDescuento() {
        assertEquals(2700, Descuentos.aplicarDescuento(3000, "frecuente"));
    }

    @Test
    @DisplayName("Un cliente con convenio recibe veinte por ciento de descuento")
    void clienteConConvenioRecibeVeintePorCientoDeDescuento() {
        assertEquals(20000, Descuentos.aplicarDescuento(25000, "convenio"));
    }

    @Test
    @DisplayName("Un cliente sin tipo especial no recibe descuento")
    void clienteSinTipoEspecialNoRecibeDescuento() {
        assertEquals(3000, Descuentos.aplicarDescuento(3000, "ninguno"));
    }

    @Test
    @DisplayName("El descuento se redondea hacia abajo al peso entero")
    void descuentoSeRedondeaHaciaAbajoAlPesoEntero() {
        assertEquals(1799, Descuentos.aplicarDescuento(1999, "frecuente"));
    }

    @ParameterizedTest(name = "cero con cliente {0} sigue siendo cero")
    @ValueSource(strings = {"ninguno", "frecuente", "convenio"})
    @DisplayName("Un valor de cero sigue siendo cero")
    void valorDeCeroSigueSiendoCero(String tipoCliente) {
        assertEquals(0, Descuentos.aplicarDescuento(0, tipoCliente));
    }

    @Test
    @DisplayName("Rechaza un valor negativo")
    void rechazaUnValorNegativo() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> Descuentos.aplicarDescuento(-1, "frecuente"));

        assertEquals("El valor no puede ser negativo", error.getMessage());
    }

    @ParameterizedTest(name = "el cliente \"{0}\" se rechaza")
    @ValueSource(strings = {"vip", "", "Frecuente", "CONVENIO"})
    @DisplayName("Rechaza un tipo de cliente desconocido")
    void rechazaUnTipoDeClienteDesconocido(String tipoCliente) {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> Descuentos.aplicarDescuento(3000, tipoCliente));

        assertEquals("Tipo de cliente no válido", error.getMessage());
    }
}
