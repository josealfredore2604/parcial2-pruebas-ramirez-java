package parqueadero.unitarias;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import parqueadero.Registro;
import parqueadero.Reportes;

/** Pruebas que protegen el comportamiento de resumen durante el refactor. */
class ReportesTest {

    @Test
    @DisplayName("Resume los registros del día")
    void resumeLosRegistrosDelDia() {
        List<Registro> registros = List.of(
                new Registro("carro", 10),
                new Registro("carro", 61),
                new Registro("moto", 600),
                new Registro("carro", 1440));

        // 0 (gracia) + 6000 (2 horas) + 12000 (tope moto) + 25000 (tope carro)
        assertEquals(Map.of("carro", 3, "moto", 1, "total_recaudado", 43000), new Reportes().resumen(registros));
    }

    @Test
    @DisplayName("Sin registros todo es cero")
    void sinRegistrosTodoEsCero() {
        assertEquals(Map.of("carro", 0, "moto", 0, "total_recaudado", 0), new Reportes().resumen(List.of()));
    }
}
