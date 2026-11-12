package parqueadero;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reportes {

    static final int tarifaCarro = 3000;

    public Map<String, Integer> resumen(List<Registro> registros) {
        Map<String, Integer> r = new HashMap<>();
        r.put("carro", 0);
        r.put("moto", 0);
        r.put("total_recaudado", 0);
        int temporal = 0;
        for (Registro reg : registros) {
            if (reg.tipo().equals("carro")) {
                r.put("carro", r.get("carro") + 1);
                if (reg.minutos() > 15) {
                    int v = (int) Math.ceil(reg.minutos() / 60.0) * tarifaCarro;
                    if (v > 25000) v = 25000;
                    r.put("total_recaudado", r.get("total_recaudado") + v);
                }
            } else if (reg.tipo().equals("moto")) {
                r.put("moto", r.get("moto") + 1);
                if (reg.minutos() > 15) {
                    int v = (int) Math.ceil(reg.minutos() / 60.0) * 1500;
                    if (v > 12000) v = 12000;
                    r.put("total_recaudado", r.get("total_recaudado") + v);
                }
            }
        }
        return r;
    }
}
