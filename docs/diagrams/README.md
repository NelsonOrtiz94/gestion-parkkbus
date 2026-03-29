# Diagramas del Sistema — ParkKbus

Este directorio contiene los diagramas arquitectónicos del sistema ParkKbus, siguiendo el modelo **C4** (Context, Container, Component, Code).

---

## Diagramas incluidos

| Archivo | Tipo | Descripción |
|---------|------|-------------|
| `context-diagram.png` | C4 Level 1 | Actores externos e integraciones del sistema |
| `container-diagram.png` | C4 Level 2 | Contenedores: app, BD, broker, clientes |
| `component-diagram.png` | C4 Level 3 | Componentes internos del Core Parking App |
| `domain-model.png` | DDD | Entidades, agregados y relaciones del dominio |
| `sequence-entry.png` | Secuencia | Flujo completo de ingreso de vehículo |
| `sequence-exit.png` | Secuencia | Flujo de salida, cálculo de tarifa y pago |
| `deployment-diagram.png` | Despliegue | Infraestructura Docker / Kubernetes |

---

## Diagrama de Contexto (C4 Level 1)

```
                    ┌──────────────────────────────┐
  [Vigilante] ──── ▶│                              │◀──── [Cajero]
  [Admin]     ──── ▶│      ParkKbus Suite          │◀──── [Auditor]
                    │  Sistema de Gestión de       │
                    │       Parqueaderos            │
                    └──────────┬───────────────────┘
                               │
              ┌────────────────┼────────────────┐
              ▼                ▼                ▼
       [PostgreSQL]     [RabbitMQ]      [Servicio de
        Base de datos    Mensajería      Notificaciones]
```

---

## Diagrama de Contenedores (C4 Level 2)

```
┌─────────────────────────────────────────────────────────┐
│                    namespace: parkkbus                   │
│                                                          │
│  ┌──────────────────┐      ┌──────────────────────────┐ │
│  │   Spring Boot    │ ───▶ │      PostgreSQL 16        │ │
│  │   Application    │      │   (postgres-service:5432) │ │
│  │   (2 réplicas)   │      └──────────────────────────┘ │
│  │   port: 8080     │                                    │
│  └──────────────────┘      ┌──────────────────────────┐ │
│           │                │      RabbitMQ             │ │
│           └──────────────▶ │   (mensajería asíncrona) │ │
│                            └──────────────────────────┘ │
└─────────────────────────────────────────────────────────┘
         ▲
  LoadBalancer:80
         ▲
  [Clientes REST / Frontend Angular / App Flutter]
```

---

## Modelo de Dominio

```
User ──────────── roles ──── Role (enum)
                               ADMIN | CAJERO | VIGILANTE | AUDITOR

Vehicle ──────────────────── ParkingSession ──────── Spot
  plate                         entryTime              code
  type                          exitTime               available
                                totalAmount
                                status (ACTIVE|CLOSED)
                                    │
                                    ▼
                                Payment
                                  amount
                                  method
                                  status

Tariff
  vehicleType
  hourlyRate
```

---

## Secuencia: Ingreso de Vehículo

```
Vigilante       ParkingEntryController    RegisterVehicleEntryUseCase    SpotRepository    SessionRepository
    │                    │                           │                        │                    │
    │── POST /entry ────▶│                           │                        │                    │
    │                    │── execute(command) ──────▶│                        │                    │
    │                    │                           │── findAvailable() ────▶│                    │
    │                    │                           │◀─ [spots] ─────────────│                    │
    │                    │                           │── save(session) ───────────────────────────▶│
    │                    │                           │◀─ [saved] ─────────────────────────────────│
    │                    │◀─ ParkingSessionResponse ─│                        │                    │
    │◀── 201 Created ────│                           │                        │                    │
```

---

## Secuencia: Salida + Cálculo + Pago

```
Cajero      ParkingExitController    RegisterVehicleExitUseCase    TariffCalculatorService
   │                 │                          │                           │
   │─ POST /exit ───▶│                          │                           │
   │                 │── execute(command) ──────▶│                          │
   │                 │                          │── findActiveByPlate() ──▶ DB
   │                 │                          │── findByVehicleType() ──▶ DB
   │                 │                          │── calculate(session, tariff) ─▶│
   │                 │                          │◀─ BigDecimal amount ──────────│
   │                 │                          │── session.closeSession()      │
   │                 │                          │── save(session) ──────── DB   │
   │                 │◀── VehicleExitResponse ──│                           │
   │◀── 200 OK ──────│                          │                           │
```

---

## Objetivo de los Diagramas

Documentar visualmente la **estructura**, los **flujos** y la **evolución arquitectónica** del sistema, alineados con las decisiones registradas en los ADR.

