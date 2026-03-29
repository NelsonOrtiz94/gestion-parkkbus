# ADR 0004 — Seguridad con JWT (JSON Web Tokens)

## Estado
Aceptado

## Fecha
2026-03-28

## Contexto
La API REST debe ser stateless y soportar múltiples roles (ADMIN, CAJERO,
VIGILANTE, AUDITOR). Se necesita un mecanismo de autenticación que no requiera
estado de sesión en el servidor y sea compatible con despliegues en contenedores.

## Decisión
Se usa **JWT (JSON Web Tokens)** con Spring Security para la autenticación y
autorización.

### Flujo de autenticación
```
Cliente → POST /api/auth/login → AuthenticateUserUseCase
        → valida credenciales con PasswordEncoder (BCrypt)
        → genera JWT via TokenServicePort
        → retorna token al cliente

Cliente → GET /api/... (Authorization: Bearer <token>)
        → JwtAuthenticationFilter valida el token
        → carga SecurityContext con roles del usuario
```

### Componentes
| Componente | Ubicación |
|-----------|-----------|
| `TokenServicePort` | `domain/port/` — contrato del dominio |
| `JwtTokenAdapter` | `infrastructure/adapter/out/security/` — implementación |
| `JwtAuthenticationFilter` | `infrastructure/config/` — filtro de Spring Security |
| `AuthenticateUserUseCase` | `application/usecase/` — lógica de autenticación |

### Roles y permisos
| Rol | Permisos |
|-----|---------|
| `ADMIN` | Acceso total |
| `CAJERO` | Registrar pagos, consultar sesiones |
| `VIGILANTE` | Registrar entradas y salidas |
| `AUDITOR` | Solo lectura: reportes y estadísticas |

## Consecuencias
- Autenticación stateless, compatible con múltiples instancias en K8s.
- El secreto JWT debe rotarse periódicamente y almacenarse en `Secret` de K8s.
- Los tokens no pueden ser revocados antes de su expiración (aceptable para este contexto).

