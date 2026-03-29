# Guía de Despliegue — ParkKbus

---

## 1. Despliegue Local con Docker Compose

### Requisitos
- Docker Desktop 4.x+
- Java 17+
- Maven (o `./mvnw`)

### Pasos

```bash
# 1. Clonar el repositorio
git clone https://github.com/NelsonOrtiz94/gestion-parkkbus.git
cd gestion-parkkbus

# 2. Levantar PostgreSQL
docker compose up -d

# 3. Ejecutar la aplicación
./mvnw spring-boot:run
```

El `compose.yaml` levanta un contenedor PostgreSQL con:
- **Puerto:** `5432`
- **Base de datos:** `parkkbus`
- **Usuario:** `postgres`
- **Contraseña:** `postgres`

---

## 2. Construcción de la Imagen Docker

El `Dockerfile` usa un **multi-stage build**:

| Etapa | Imagen base | Propósito |
|-------|-------------|-----------|
| `builder` | `eclipse-temurin:17-jdk-alpine` | Compila y empaqueta con Maven |
| `runtime` | `eclipse-temurin:17-jre-alpine` | Imagen final ligera (~180 MB) |

```bash
# Compilar sin tests
./mvnw clean package -DskipTests

# Construir imagen Docker
docker build -t gestion-parkkbus:latest .

# Ejecutar contenedor
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/parkkbus \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=postgres \
  gestion-parkkbus:latest
```

---

## 3. Despliegue en Kubernetes

### Requisitos
- Kubernetes 1.25+ (minikube, kind o clúster real)
- `kubectl` configurado
- Imagen Docker disponible en un registry accesible

### Manifiestos en `k8s/`

| Archivo | Tipo | Descripción |
|---------|------|-------------|
| `namespace.yaml` | `Namespace` | Crea el namespace `parkkbus` aislado |
| `configmap.yaml` | `ConfigMap` | Variables no sensibles: URL de BD, perfil activo |
| `secret.yaml` | `Secret` | Credenciales de BD y secreto JWT |
| `postgres-deployment.yaml` | `Deployment` | Pod de PostgreSQL (1 réplica) |
| `postgres-service.yaml` | `Service` (ClusterIP) | Expone PostgreSQL internamente en puerto 5432 |
| `app-deployment.yaml` | `Deployment` | Pods de la aplicación (2 réplicas) con health probes |
| `app-service.yaml` | `Service` (LoadBalancer) | Expone la aplicación externamente en puerto 80 |

### Orden de despliegue

```bash
# 1. Crear el namespace
kubectl apply -f k8s/namespace.yaml

# 2. Configuración
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secret.yaml

# 3. Base de datos
kubectl apply -f k8s/postgres-deployment.yaml
kubectl apply -f k8s/postgres-service.yaml

# 4. Aplicación (solo después de que PostgreSQL esté listo)
kubectl apply -f k8s/app-deployment.yaml
kubectl apply -f k8s/app-service.yaml
```

### Verificar el estado

```bash
# Ver todos los recursos en el namespace
kubectl get all -n parkkbus

# Ver logs de la aplicación
kubectl logs -n parkkbus -l app=parkkbus-app --tail=50

# Ver logs de PostgreSQL
kubectl logs -n parkkbus -l app=postgres --tail=50
```

---

## 4. Variables de Configuración

### ConfigMap (`configmap.yaml`)

| Variable | Valor | Descripción |
|----------|-------|-------------|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://postgres-service:5432/parkkbus` | URL de conexión a BD |
| `SPRING_DATASOURCE_USERNAME` | `parkkbus` | Usuario de BD |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `validate` | No modifica el schema en producción |
| `SPRING_PROFILES_ACTIVE` | `prod` | Perfil activo |

### Secret (`secret.yaml`)

| Variable | Descripción |
|----------|-------------|
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de la base de datos |
| `JWT_SECRET` | Secreto para firma de tokens JWT |

> ⚠️ **En producción:** los secretos deben gestionarse con herramientas como Sealed Secrets, Vault o el gestor de secretos del proveedor cloud.

---

## 5. Health Checks

La aplicación expone health probes usados por Kubernetes:

| Probe | Endpoint | Descripción |
|-------|----------|-------------|
| `readinessProbe` | `/actuator/health/readiness` | ¿Listo para recibir tráfico? |
| `livenessProbe` | `/actuator/health/liveness` | ¿Sigue vivo el proceso? |

Configurados en `k8s/app-deployment.yaml`:
- **Readiness:** inicio en 15s, cada 10s
- **Liveness:** inicio en 30s, cada 20s

---

## 6. Recursos por Pod (Aplicación)

| Tipo | CPU | Memoria |
|------|-----|---------|
| Request | `250m` | `256Mi` |
| Limit | `500m` | `512Mi` |

---

## 7. Escalamiento

```bash
# Escalar la aplicación a 3 réplicas
kubectl scale deployment parkkbus-app --replicas=3 -n parkkbus

# Verificar
kubectl get pods -n parkkbus
```

