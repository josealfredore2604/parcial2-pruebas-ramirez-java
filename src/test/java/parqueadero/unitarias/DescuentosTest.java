package parqueadero.unitarias;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}
