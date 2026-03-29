# ADR 0002 — Arquitectura Hexagonal (Ports & Adapters)

## Estado
Aceptado

## Fecha
2026-03-28

## Contexto
La aplicación debe ser independiente de frameworks, bases de datos y mecanismos
de entrega (REST, mensajería). Se necesita una estructura que permita intercambiar
adaptadores sin reescribir la lógica de negocio.

## Decisión
Se adopta la **Arquitectura Hexagonal** (Ports & Adapters) de Alistair Cockburn.

### Estructura de paquetes
```
domain/port/          ← interfaces (contratos del dominio)
infrastructure/adapter/in/web/      ← adaptador REST (entrada)
infrastructure/adapter/out/persistence/  ← adaptador JPA (salida)
infrastructure/adapter/out/security/     ← adaptador JWT (salida)
infrastructure/adapter/out/messaging/    ← adaptador eventos (salida)
infrastructure/adapter/out/notification/ ← adaptador notificaciones (salida)
```

### Regla de dependencias
```
infrastructure → application → domain
```
El dominio no conoce ni importa nada de las capas externas.

## Consecuencias
- Los adaptadores pueden ser reemplazados (p.ej. cambiar JPA por MongoDB) sin tocar la lógica.
- Los tests unitarios del dominio y casos de uso usan mocks de los puertos.
- Los tests de integración verifican los adaptadores reales contra contenedores Docker.

