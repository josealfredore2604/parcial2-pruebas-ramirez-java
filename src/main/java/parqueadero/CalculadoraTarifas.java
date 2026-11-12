package parqueadero;

import java.util.Map;

/** Cálculo de la tarifa de un parqueadero. */
public class CalculadoraTarifas {

    private static final int MINUTOS_DE_GRACIA = 15;
    private static final int MINUTOS_MAXIMOS = 1440;
    private static final Map<String, Integer> TARIFA_POR_HORA = Map.of("carro", 3000, "moto", 1500);
    private static final Map<String, Integer> TOPE_DIARIO = Map.of("carro", 25000, "moto", 12000);

    /**
     * Calcula el valor a pagar, en pesos, por una estadía en el parqueadero.
     * Los primeros 15 minutos son gratis. Después se cobra cada hora o fracción
     * de hora según el tipo de vehículo, sin superar el tope diario.
     */
    public int calcularTarifa(int minutos, String tipoVehiculo) {
        if (!TARIFA_POR_HORA.containsKey(tipoVehiculo)) {
            throw new IllegalArgumentException("Tipo de vehículo no válido");
        }
        if (minutos < 0 || minutos > MINUTOS_MAXIMOS) {
            throw new IllegalArgumentException("Los minutos deben estar entre 0 y 1440");
        }

        if (minutos <= MINUTOS_DE_GRACIA) {
            return 0;
        }

        int horasCobradas = (minutos + 59) / 60;
        int valor = horasCobradas * TARIFA_POR_HORA.get(tipoVehiculo);
        return Math.min(valor, TOPE_DIARIO.get(tipoVehiculo));
    }
}
