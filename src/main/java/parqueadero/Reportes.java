package parqueadero;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Reporte del día del parqueadero. */
public class Reportes {

    private static final String CARRO = "carro";
    private static final String MOTO = "moto";
    private static final String TOTAL_RECAUDADO = "total_recaudado";

    private final CalculadoraTarifas calculadora = new CalculadoraTarifas();

    /** Cuenta los carros y las motos del día y suma lo recaudado. Los tipos de vehículo son válidos. */
    public Map<String, Integer> resumen(List<Registro> registros) {
        Map<String, Integer> reporte = new HashMap<>();
        reporte.put(CARRO, 0);
        reporte.put(MOTO, 0);
        reporte.put(TOTAL_RECAUDADO, 0);

        for (Registro registro : registros) {
            int valorAPagar = calculadora.calcularTarifa(registro.minutos(), registro.tipo());
            reporte.merge(registro.tipo(), 1, Integer::sum);
            reporte.merge(TOTAL_RECAUDADO, valorAPagar, Integer::sum);
        }
        return reporte;
    }
}
