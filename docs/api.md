# API Reference — ParkKbus

Base URL: `http://localhost:8080`

Todos los endpoints (excepto `/auth/login`) requieren el header:
```
Authorization: Bearer <token>
```

---

## 🔑 Autenticación

### POST `/auth/login`
Autentica un usuario y devuelve un token JWT.

**Request:**
```json
{
  "username": "admin",
  "password": "123456"
}
```

**Response `200 OK`:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Errores:**
| Código | Descripción |
|--------|-------------|
| `400` | Credenciales inválidas o usuario inactivo |
| `404` | Usuario no encontrado |

---

## 🚗 Gestión de Parqueadero

### POST `/api/parking/entry`
Registra la entrada de un vehículo y asigna un espacio disponible.

**Roles requeridos:** `VIGILANTE`, `ADMIN`

**Request:**
```json
{
  "plate": "ABC-123",
  "vehicleType": "CAR"
}
```

**Tipos de vehículo válidos:** `CAR`, `MOTORCYCLE`, `TRUCK`

**Response `201 Created`:**
```json
{
  "sessionId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "plate": "ABC-123",
  "spotCode": "A-01",
  "entryTime": "2026-03-28T10:00:00",
  "status": "ACTIVE"
}
```

**Errores:**
| Código | Descripción |
|--------|-------------|
| `409` | No hay espacios disponibles |

---

### POST `/api/parking/exit`
Registra la salida de un vehículo y calcula el monto a pagar.

**Roles requeridos:** `VIGILANTE`, `ADMIN`

**Request:**
```json
{
  "plate": "ABC-123"
}
```

**Response `200 OK`:**
```json
{
  "sessionId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "plate": "ABC-123",
  "entryTime": "2026-03-28T10:00:00",
  "exitTime": "2026-03-28T12:30:00",
  "totalAmount": 6000.00
}
```

**Errores:**
| Código | Descripción |
|--------|-------------|
| `400` | No existe sesión activa para esa placa |
| `400` | No hay tarifa configurada para ese tipo de vehículo |

---

## 📊 Reportes

### GET `/api/reports/spots/available`
Devuelve la lista de espacios disponibles.

**Roles requeridos:** Cualquier rol autenticado

**Response `200 OK`:**
```json
[
  { "id": "uuid", "code": "A-01", "available": true },
  { "id": "uuid", "code": "A-02", "available": true }
]
```

---

### GET `/api/reports/occupancy`
Genera un reporte de ocupación para un rango de fechas.

**Roles requeridos:** `AUDITOR`, `ADMIN`

**Query params:**
| Param | Tipo | Ejemplo |
|-------|------|---------|
| `from` | `LocalDate` (ISO) | `2026-03-01` |
| `to` | `LocalDate` (ISO) | `2026-03-28` |

**Ejemplo:**
```
GET /api/reports/occupancy?from=2026-03-01&to=2026-03-28
```

**Response `200 OK`:**
```json
{
  "period": "2026-03-01 / 2026-03-28",
  "totalEntries": 145,
  "totalExits": 140,
  "totalRevenue": 435000.00,
  "occupancyRate": 72.5
}
```

---

## ⚠️ Manejo de errores

Todos los errores siguen este formato:

```json
{
  "timestamp": "2026-03-28T10:00:00",
  "error": "Descripción del error"
}
```

| Código HTTP | Situación |
|-------------|-----------|
| `400 Bad Request` | Parámetros inválidos o regla de negocio violada |
| `401 Unauthorized` | Token ausente o inválido |
| `403 Forbidden` | Rol insuficiente |
| `404 Not Found` | Recurso no encontrado |
| `409 Conflict` | Estado inconsistente (ej: no hay espacios) |
| `500 Internal Server Error` | Error inesperado |

---

## 🔢 Lógica de tarifas

El cálculo de tarifa aplica las siguientes reglas:

- La unidad mínima de cobro es **1 hora** (aunque la estadía sea menor)
- Se aplica **redondeo al alza** por hora parcial
- Ejemplo: 90 minutos = 2 horas facturadas

| Tipo de vehículo | Tarifa por hora |
|-----------------|----------------|
| `CAR` | $3,000 |
| `MOTORCYCLE` | $1,500 |
| `TRUCK` | $5,000 |

