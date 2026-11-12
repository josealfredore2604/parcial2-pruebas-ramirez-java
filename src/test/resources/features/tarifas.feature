# language: es
Característica: Cobro del parqueadero EstacionaYa
  Como dueño del parqueadero
  quiero que cada vehículo pague según el tiempo que estuvo y el tipo de cliente
  para cobrar de forma justa y predecible.

  Esquema del escenario: Cobro de una estadía según el tipo de vehículo y el tiempo
    Dado un <vehiculo> que permaneció <minutos> minutos en el parqueadero
    Cuando se calcula el valor a pagar
    Entonces el valor a pagar es <valor> pesos

    Ejemplos: Los primeros 15 minutos son gratis
      | vehiculo | minutos | valor |
      | carro    | 15      | 0     |
      | moto     | 15      | 0     |

    Ejemplos: Pasada la gracia se cobra cada hora o fracción de hora
      | vehiculo | minutos | valor |
      | carro    | 16      | 3000  |
      | carro    | 60      | 3000  |
      | carro    | 61      | 6000  |
      | moto     | 16      | 1500  |
      | moto     | 60      | 1500  |
      | moto     | 61      | 3000  |

    Ejemplos: Nadie paga más del tope diario
      | vehiculo | minutos | valor |
      | carro    | 480     | 24000 |
      | carro    | 481     | 25000 |
      | carro    | 1440    | 25000 |
      | moto     | 480     | 12000 |
      | moto     | 481     | 12000 |
      | moto     | 1440    | 12000 |

  Escenario: Un cliente frecuente recibe diez por ciento de descuento
    Dado un valor a pagar de 3000 pesos
    Cuando se aplica el descuento de un cliente frecuente
    Entonces el cliente paga 2700 pesos

  Escenario: Un cliente con convenio recibe veinte por ciento de descuento
    Dado un valor a pagar de 25000 pesos
    Cuando se aplica el descuento de un cliente convenio
    Entonces el cliente paga 20000 pesos

  Escenario: Un cliente sin descuento paga el valor completo
    Dado un valor a pagar de 3000 pesos
    Cuando se aplica el descuento de un cliente ninguno
    Entonces el cliente paga 3000 pesos

  Escenario: El descuento se redondea hacia abajo al peso entero
    Dado un valor a pagar de 1999 pesos
    Cuando se aplica el descuento de un cliente frecuente
    Entonces el cliente paga 1799 pesos

  Escenario: Un vehículo que no es carro ni moto no se puede cobrar
    Dado un bicicleta que permaneció 30 minutos en el parqueadero
    Cuando se intenta calcular el valor a pagar
    Entonces el sistema responde "Tipo de vehículo no válido"

  Escenario: Una estadía de más de 24 horas no se puede cobrar
    Dado un carro que permaneció 1441 minutos en el parqueadero
    Cuando se intenta calcular el valor a pagar
    Entonces el sistema responde "Los minutos deben estar entre 0 y 1440"

  Escenario: Un tipo de cliente desconocido no recibe descuento
    Dado un valor a pagar de 3000 pesos
    Cuando se intenta aplicar el descuento de un cliente vip
    Entonces el sistema responde "Tipo de cliente no válido"
