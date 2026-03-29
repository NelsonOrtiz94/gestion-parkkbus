# ADR 0005 — Contenerización con Docker y Orquestación con Kubernetes

## Estado
Aceptado

## Fecha
2026-03-28

## Contexto
El sistema debe desplegarse de forma reproducible en diferentes entornos
(desarrollo, staging, producción). Se requiere escalabilidad horizontal y
resiliencia ante fallos de instancias individuales.

## Decisión
Se adopta **Docker** para contenerización y **Kubernetes (K8s)** para orquestación.

### Estrategia Docker
- **Multi-stage build** en `Dockerfile`: etapa `builder` (JDK 17) + etapa de runtime (JRE 17 Alpine).
- Imagen final < 200 MB basada en `eclipse-temurin:17-jre-alpine`.
- `compose.yaml` para desarrollo local con PostgreSQL.

### Estructura Kubernetes (`k8s/`)
```
namespace.yaml          ← namespace aislado "parkkbus"
configmap.yaml          ← variables de entorno no sensibles
secret.yaml             ← credenciales y secretos (base64)
postgres-deployment.yaml ← despliegue de PostgreSQL
postgres-service.yaml   ← servicio ClusterIP para PostgreSQL
app-deployment.yaml     ← despliegue de la aplicación (2 réplicas)
app-service.yaml        ← servicio LoadBalancer expuesto en puerto 80
```

### Health checks
La aplicación expone `/actuator/health/readiness` y `/actuator/health/liveness`
usados por los probes de K8s para garantizar disponibilidad.

### Recursos por pod (app)
| Tipo | CPU | Memoria |
|------|-----|---------|
| Request | 250m | 256Mi |
| Limit | 500m | 512Mi |

## Consecuencias
- Despliegue reproducible en cualquier clúster K8s estándar.
- Escalado horizontal con `kubectl scale deployment parkkbus-app --replicas=N`.
- Los secretos deben gestionarse con herramientas como Sealed Secrets o Vault en producción.
- Se requiere un registry de imágenes (Docker Hub, ECR, GCR) para el pipeline CI/CD.

