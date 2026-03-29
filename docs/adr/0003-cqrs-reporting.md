# ADR 0003 — CQRS para Reportes y Consultas

## Estado
Aceptado

## Fecha
2026-03-28

## Contexto
Las operaciones de escritura (registrar entrada/salida, procesar pagos) tienen
requisitos de consistencia estrictos, mientras que los reportes de ocupación y
estadísticas son consultas de solo lectura que pueden tolerar ligera latencia
y requieren proyecciones optimizadas.

## Decisión
Se aplica el patrón **CQRS (Command Query Responsibility Segregation)** a nivel
de la capa de aplicación:

### Commands (escritura)
| Comando | Descripción |
|---------|-------------|
| `RegisterVehicleEntryUseCase` | Registra la entrada de un vehículo |
| `RegisterVehicleExitUseCase` | Registra la salida y calcula tarifa |
| `ProcessPaymentUseCase` | Procesa el pago de una sesión |

### Queries (lectura)
| Query | Descripción |
|-------|-------------|
| `GetAvailableSpotsQueryUseCase` | Consulta espacios disponibles |
| `GetOccupancyReportQueryUseCase` | Genera reporte de ocupación |

### Paquetes
```
application/command/   ← objetos de comando (entrada de escritura)
application/query/     ← objetos de consulta (entrada de lectura)
application/usecase/   ← implementaciones de casos de uso
application/dto/       ← objetos de transferencia de datos (respuestas)
```

## Consecuencias
- Las consultas pueden optimizarse con proyecciones específicas sin afectar comandos.
- Facilita escalar el lado de lectura independientemente.
- Añade algo de complejidad de estructura pero mejora la mantenibilidad a largo plazo.

