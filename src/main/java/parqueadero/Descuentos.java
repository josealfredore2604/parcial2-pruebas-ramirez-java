package parqueadero;

/** Descuentos para los clientes del parqueadero. */
public final class Descuentos {

    private Descuentos() {
    }

    /** Devuelve el valor a pagar después de aplicar el descuento del cliente. */
    public static int aplicarDescuento(int valor, String tipoCliente) {
        if ("ninguno".equals(tipoCliente)) {
            return valor;
        }
        if ("convenio".equals(tipoCliente)) {
            return valor * 80 / 100;
        }
        return valor * 90 / 100;
    }
}
