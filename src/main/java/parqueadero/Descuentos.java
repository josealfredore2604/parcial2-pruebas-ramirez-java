package parqueadero;

import java.util.Map;

/** Descuentos para los clientes del parqueadero. */
public final class Descuentos {

    private static final int CIEN_POR_CIENTO = 100;
    private static final Map<String, Integer> PORCENTAJE_DE_DESCUENTO =
            Map.of("ninguno", 0, "frecuente", 10, "convenio", 20);

    private Descuentos() {
    }

    /**
     * Devuelve el valor a pagar después de aplicar el descuento del cliente.
     * El resultado se redondea hacia abajo al peso entero.
     */
    public static int aplicarDescuento(int valor, String tipoCliente) {
        if (!PORCENTAJE_DE_DESCUENTO.containsKey(tipoCliente)) {
            throw new IllegalArgumentException("Tipo de cliente no válido");
        }
        if (valor < 0) {
            throw new IllegalArgumentException("El valor no puede ser negativo");
        }

        int porcentajeAPagar = CIEN_POR_CIENTO - PORCENTAJE_DE_DESCUENTO.get(tipoCliente);
        return valor * porcentajeAPagar / CIEN_POR_CIENTO;
    }
}
