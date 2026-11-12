package parqueadero;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reporte del día del parqueadero.
 *
 * <p>El resumen no repite las reglas de cobro: delega el valor de cada estadía en
 * {@link CalculadoraTarifas}.
 */
public class Reportes {

    private static final String CARRO = "carro";
    private static final String MOTO = "moto";
    private static final String TOTAL_RECAUDADO = "total_recaudado";

    private final CalculadoraTarifas calculadora = new CalculadoraTarifas();

    /**
     * Cuenta los carros y las motos del día y suma lo recaudado.
     *
     * @param registros estadías del día; todas tienen un tipo de vehículo válido
     * @return un mapa con tres claves: {@code "carro"} y {@code "moto"} (cantidad de
     *     vehículos de cada tipo) y {@code "total_recaudado"} (suma en pesos)
     * @throws IllegalArgumentException si algún registro tiene minutos fuera del rango válido
     *     (lo lanza {@link CalculadoraTarifas#calcularTarifa(int, String)})
     */
    public Map<String, Integer> resumen(List<Registro> registros) {
        Map<String, Integer> reporte = new HashMap<>();
        reporte.put(CARRO, 0);
        reporte.put(MOTO, 0);
        reporte.put(TOTAL_RECAUDADO, 0);

        for (Registro registro : registros) {
            int valorAPagar = calculadora.calcularTarifa(registro.minutos(), registro.tipo());
            // merge suma 1 (o el valor) a lo que ya hay en la clave, con Integer::sum.
            reporte.merge(registro.tipo(), 1, Integer::sum);
            reporte.merge(TOTAL_RECAUDADO, valorAPagar, Integer::sum);
        }
        return reporte;
    }
}
