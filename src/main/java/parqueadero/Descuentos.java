package parqueadero;

import java.util.Map;

/**
 * Descuentos para los clientes del parqueadero.
 *
 * <p>Cada tipo de cliente tiene un porcentaje de descuento fijo:
 * {@code ninguno} 0 %, {@code frecuente} 10 % y {@code convenio} 20 %.
 *
 * <p>Es una clase de utilidad: no tiene estado, por eso es {@code final}, tiene un
 * constructor privado y solo expone un método estático.
 */
public final class Descuentos {

    /** Base de los porcentajes; evita repetir el número 100 en el cálculo. */
    private static final int CIEN_POR_CIENTO = 100;

    /** Porcentaje de descuento (entero) por tipo de cliente. */
    private static final Map<String, Integer> PORCENTAJE_DE_DESCUENTO =
            Map.of("ninguno", 0, "frecuente", 10, "convenio", 20);

    /** Evita que se creen instancias: la clase solo agrupa un método estático. */
    private Descuentos() {
    }

    /**
     * Devuelve el valor a pagar después de aplicar el descuento del cliente.
     * El resultado se redondea hacia abajo al peso entero.
     *
     * @param valor valor original a pagar, en pesos; no puede ser negativo
     * @param tipoCliente tipo de cliente: {@code "ninguno"}, {@code "frecuente"} o
     *     {@code "convenio"}
     * @return el valor con el descuento aplicado, en pesos enteros
     * @throws IllegalArgumentException si el tipo de cliente no es válido o si el valor es
     *     negativo; el tipo de cliente se valida primero
     */
    public static int aplicarDescuento(int valor, String tipoCliente) {
        if (!PORCENTAJE_DE_DESCUENTO.containsKey(tipoCliente)) {
            throw new IllegalArgumentException("Tipo de cliente no válido");
        }
        if (valor < 0) {
            throw new IllegalArgumentException("El valor no puede ser negativo");
        }

        int porcentajeAPagar = CIEN_POR_CIENTO - PORCENTAJE_DE_DESCUENTO.get(tipoCliente);
        // La división entera de int descarta los decimales, es decir, redondea hacia abajo.
        return valor * porcentajeAPagar / CIEN_POR_CIENTO;
    }
}
