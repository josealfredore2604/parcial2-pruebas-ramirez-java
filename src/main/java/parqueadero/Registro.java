package parqueadero;

/**
 * Una estadía en el parqueadero: tipo de vehículo y minutos de permanencia.
 *
 * <p>Es un {@code record}: una clase inmutable de datos. Java genera por sí solo el
 * constructor, los métodos de acceso {@code tipo()} y {@code minutos()},
 * y también {@code equals}, {@code hashCode} y {@code toString}.
 *
 * @param tipo tipo de vehículo, {@code "carro"} o {@code "moto"}
 * @param minutos duración de la estadía en minutos
 */
public record Registro(String tipo, int minutos) {
}
