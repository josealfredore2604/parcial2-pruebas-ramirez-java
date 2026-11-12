package parqueadero;

import java.util.Map;

/**
 * Cálculo de la tarifa de un parqueadero.
 *
 * <p>Reglas que implementa esta clase:
 * <ul>
 *   <li>Solo existen los tipos de vehículo {@code carro} y {@code moto}.</li>
 *   <li>La duración válida va de 0 a 1440 minutos (24 horas), ambos incluidos.</li>
 *   <li>Los primeros 15 minutos son gratis (minutos de gracia).</li>
 *   <li>Después de la gracia se cobra cada hora o fracción de hora.</li>
 *   <li>Ninguna estadía paga más del tope diario de su tipo de vehículo.</li>
 * </ul>
 */
public class CalculadoraTarifas {

    /** Minutos que no se cobran al inicio de la estadía. */
    private static final int MINUTOS_DE_GRACIA = 15;

    /** Duración máxima aceptada para una estadía, en minutos (24 horas). */
    private static final int MINUTOS_MAXIMOS = 1440;

    /** Valor de cada hora o fracción de hora, en pesos, por tipo de vehículo. */
    private static final Map<String, Integer> TARIFA_POR_HORA = Map.of("carro", 3000, "moto", 1500);

    /** Valor máximo que puede pagar una estadía en un día, en pesos, por tipo de vehículo. */
    private static final Map<String, Integer> TOPE_DIARIO = Map.of("carro", 25000, "moto", 12000);

    /**
     * Calcula el valor a pagar, en pesos, por una estadía en el parqueadero.
     * Los primeros 15 minutos son gratis. Después se cobra cada hora o fracción
     * de hora según el tipo de vehículo, sin superar el tope diario.
     *
     * @param minutos duración de la estadía, entre 0 y 1440 minutos
     * @param tipoVehiculo tipo de vehículo, {@code "carro"} o {@code "moto"}
     * @return el valor a pagar en pesos; es 0 si la estadía está dentro de la gracia
     * @throws IllegalArgumentException si el tipo de vehículo no es válido o si los minutos
     *     están fuera del rango de 0 a 1440; el tipo de vehículo se valida primero
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

        // Truco de división entera para redondear hacia arriba: (61 + 59) / 60 = 2 horas.
        int horasCobradas = (minutos + 59) / 60;
        int valor = horasCobradas * TARIFA_POR_HORA.get(tipoVehiculo);
        return Math.min(valor, TOPE_DIARIO.get(tipoVehiculo));
    }
}
