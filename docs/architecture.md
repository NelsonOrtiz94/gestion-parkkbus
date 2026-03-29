# Arquitectura del Sistema — ParkKbus

## 1. Contexto

ParkKbus es un sistema de gestión de parqueaderos que centraliza el control de:
- Ingreso y salida de vehículos
- Asignación de espacios disponibles
- Cálculo automático de tarifas
- Procesamiento de pagos
- Autenticación y autorización por roles
- Reportes de ocupación e ingresos

El sistema nació como un monolito y fue re-arquitectado siguiendo principios de DDD, arquitectura hexagonal y CQRS parcial para mejorar mantenibilidad, testabilidad y escalabilidad.

---

## 2. Estilo Arquitectónico

### 2.1 Domain-Driven Design (DDD)
El núcleo del sistema está modelado en torno al dominio del negocio. Los conceptos como `ParkingSession`, `Vehicle`, `Spot`, `Tariff` y `Payment` son objetos del dominio puro, sin dependencias de frameworks.

### 2.2 Arquitectura Hexagonal (Ports & Adapters)
La capa de dominio define interfaces (puertos) que son implementadas por adaptadores en la infraestructura. El dominio nunca depende de detalles tecnológicos.

```
┌─────────────────────────────────────────────────┐
│                 INFRASTRUCTURE                  │
│  ┌──────────────────────────────────────────┐   │
│  │              APPLICATION                 │   │
│  │  ┌────────────────────────────────────┐  │   │
│  │  │             DOMAIN                 │  │   │
│  │  │  model / port / service / event    │  │   │
│  │  └────────────────────────────────────┘  │   │
│  │  usecase / command / query / dto         │   │
│  └──────────────────────────────────────────┘   │
│  adapter/in/web   adapter/out/(jpa/jwt/mq)       │
└─────────────────────────────────────────────────┘
```

**Regla de dependencias:**
```
infrastructure → application → domain
```
El dominio no conoce ni importa nada de capas externas.

### 2.3 CQRS Parcial
Se aplica separación de comandos y consultas a nivel de capa de aplicación:

| Tipo | Paquete | Ejemplos |
|------|---------|---------|
| **Commands** (escritura) | `application/command/` | `RegisterVehicleEntryCommand`, `ProcessPaymentCommand` |
| **Queries** (lectura) | `application/query/` | `GetAvailableSpotsQuery`, `GetOccupancyReportQuery` |

---

## 3. Capas del Sistema

### 3.1 Domain (`domain/`)
- **`model/`** — Entidades y value objects puros: `User`, `Vehicle`, `ParkingSession`, `Spot`, `Tariff`, `Payment`, `Role`
- **`port/`** — Interfaces que definen contratos hacia el exterior: `UserRepositoryPort`, `TokenServicePort`, `ParkingSessionRepositoryPort`, etc.
- **`service/`** — Servicios de dominio: `TariffCalculatorService` (implementa patrón Strategy)
- **`event/`** — Eventos de dominio: `VehicleEnteredEvent`, `VehicleExitedEvent`, `PaymentProcessedEvent`

### 3.2 Application (`application/`)
- **`usecase/`** — Orquestadores de lógica: `AuthenticateUserUseCase`, `RegisterVehicleEntryUseCase`, `RegisterVehicleExitUseCase`, `ProcessPaymentUseCase`, `GetAvailableSpotsQueryUseCase`, `GetOccupancyReportQueryUseCase`
- **`command/`** — Objetos de entrada para escrituras CQRS
- **`query/`** — Objetos de entrada para consultas CQRS
- **`dto/`** — Objetos de respuesta: `ParkingSessionResponse`, `VehicleExitResponse`, `SpotResponse`, `OccupancyReportResponse`

### 3.3 Infrastructure (`infrastructure/`)
- **`adapter/in/web/`** — Controladores REST: `AuthController`, `ParkingEntryController`, `ParkingExitController`, `ReportController`
- **`adapter/out/security/`** — Adaptador JWT: `JwtTokenAdapter` implementa `TokenServicePort`
- **`adapter/out/persistence/`** — Adaptadores JPA (pendiente de implementación)
- **`adapter/out/messaging/`** — Publicación de eventos vía RabbitMQ (pendiente)
- **`adapter/out/notification/`** — Notificaciones (pendiente)
- **`config/`** — Configuración Spring: `SecurityConfig`, `UseCaseConfig`

### 3.4 Shared (`shared/`)
- **`exception/`** — `GlobalExceptionHandler`, `ResourceNotFoundException`
- **`util/`** — Utilidades transversales

---

## 4. Componentes Principales

### Autenticación
```
POST /auth/login
  → AuthController
  → AuthenticateUserUseCase
  → UserRepositoryPort (valida usuario)
  → PasswordEncoder (BCrypt)
  → TokenServicePort → JwtTokenAdapter (genera JWT)
  ← token JWT
```

### Ingreso de vehículo
```
POST /api/parking/entry
  → ParkingEntryController
  → RegisterVehicleEntryUseCase
  → SpotRepositoryPort (busca espacio disponible)
  → ParkingSessionRepositoryPort (guarda sesión)
  ← ParkingSessionResponse
```

### Salida + cálculo de tarifa
```
POST /api/parking/exit
  → ParkingExitController
  → RegisterVehicleExitUseCase
  → ParkingSessionRepositoryPort (busca sesión activa)
  → TariffRepositoryPort (busca tarifa por tipo de vehículo)
  → TariffCalculatorService (Strategy: calcula monto)
  ← VehicleExitResponse (con totalAmount)
```

### Reportes (CQRS — solo lectura)
```
GET /api/reports/occupancy?from=&to=
  → ReportController
  → GetOccupancyReportQueryUseCase
  → ParkingSessionRepositoryPort (métodos de lectura)
  ← OccupancyReportResponse
```

---

## 5. Seguridad

| Componente | Implementación |
|-----------|---------------|
| Autenticación | JWT (jjwt 0.12.x) |
| Hashing de contraseñas | BCrypt (Spring Security) |
| Autorización | Roles: `ADMIN`, `CAJERO`, `VIGILANTE`, `AUDITOR` |
| Puerto de dominio | `TokenServicePort` |
| Adaptador JWT | `JwtTokenAdapter` en `infrastructure/adapter/out/security/` |

---

## 6. Persistencia

- **Motor:** PostgreSQL 16
- **ORM:** Spring Data JPA / Hibernate
- **Migraciones:** Flyway (`src/main/resources/db/migration/`)
- **Script inicial:** `V1__initial_schema.sql` — crea tablas y semillas básicas

---

## 7. Integración y Despliegue

### Docker
- **Multi-stage build:** `eclipse-temurin:17-jdk-alpine` → `eclipse-temurin:17-jre-alpine`
- Imagen final ~180 MB
- `compose.yaml` para desarrollo local con PostgreSQL

### Kubernetes (`k8s/`)
- Namespace aislado: `parkkbus`
- ConfigMap para variables no sensibles
- Secret para credenciales y JWT secret
- 2 réplicas de la aplicación con health probes
- PostgreSQL con ClusterIP
- Aplicación expuesta con LoadBalancer en puerto 80

---

## 8. ADR Relacionados

| ADR | Decisión |
|-----|---------|
| [0001-ddd.md](adr/0001-ddd.md) | Adopción de DDD |
| [0002-hexagonal.md](adr/0002-hexagonal.md) | Arquitectura Hexagonal |
| [0003-cqrs-reporting.md](adr/0003-cqrs-reporting.md) | CQRS para reportes |
| [0004-jwt-security.md](adr/0004-jwt-security.md) | Seguridad con JWT |
| [0005-docker-k8s.md](adr/0005-docker-k8s.md) | Docker y Kubernetes |

---

## 9. Diagramas

Los diagramas del sistema se encuentran en [`docs/diagrams/`](diagrams/).

| Diagrama | Descripción |
|---------|-------------|
| `context-diagram` | C4 Level 1 — actores y sistemas externos |
| `container-diagram` | C4 Level 2 — contenedores del sistema |
| `component-diagram` | C4 Level 3 — componentes internos |
| `domain-model` | Modelo de dominio DDD |
| `sequence-entry` | Secuencia: ingreso de vehículo |
| `sequence-exit` | Secuencia: salida + pago |

