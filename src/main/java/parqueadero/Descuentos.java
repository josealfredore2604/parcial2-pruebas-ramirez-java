package parqueadero;

/** Descuentos para los clientes del parqueadero. */
public final class Descuentos {

    private Descuentos() {
    }

    /** Devuelve el valor a pagar después de aplicar el descuento del cliente. */
    public static int aplicarDescuento(int valor, String tipoCliente) {
        if (valor < 0) {
            throw new IllegalArgumentException("El valor no puede ser negativo");
        }
        if ("ninguno".equals(tipoCliente)) {
            return valor;
        }
        if ("convenio".equals(tipoCliente)) {
            return valor * 80 / 100;
        }
        if ("frecuente".equals(tipoCliente)) {
            return valor * 90 / 100;
        }
        throw new IllegalArgumentException("Tipo de cliente no válido");
    }
}
