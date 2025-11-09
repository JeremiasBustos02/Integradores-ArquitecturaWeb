# 🏗️ Arquitectura del Sistema - Vista General

## 📊 Diagrama de Arquitectura

```
┌─────────────────────────────────────────────────────────────────┐
│                         CLIENTE                                  │
│                      (Aplicación Web/Móvil)                      │
└─────────────────────┬───────────────────────────────────────────┘
                      │ HTTP/REST
                      ▼
┌─────────────────────────────────────────────────────────────────┐
│                    API GATEWAY (8080)                            │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  • Autenticación JWT                                      │  │
│  │  • Enrutamiento de requests                               │  │
│  │  • Balanceo de carga                                      │  │
│  │  DB: auth_db (MySQL)                                      │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────┬───────────────────────────────────────────────────┘
              │
              ▼
┌─────────────────────────────────────────────────────────────────┐
│              EUREKA SERVER (8761)                                │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  • Service Discovery                                      │  │
│  │  • Registro de microservicios                             │  │
│  │  • Health checks                                          │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
              │
              ▼
┌─────────────────────────────────────────────────────────────────┐
│              CONFIG SERVER (8081)                                │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  • Configuración centralizada                             │  │
│  │  • Gestión de propiedades                                 │  │
│  │  • Perfiles de entorno                                    │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
              │
              ▼
┌─────────────────────────────────────────────────────────────────┐
│                     MICROSERVICIOS                               │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌──────────────────────┐  ┌──────────────────────┐            │
│  │  USUARIOS (8083)     │  │  TARIFAS (8084)      │            │
│  │  • Gestión usuarios  │  │  • Gestión tarifas   │            │
│  │  • Gestión cuentas   │  │  • Precios activos   │            │
│  │  • Relaciones U-C    │  │  • Vigencias         │            │
│  │  DB: usuarios_db     │  │  DB: tarifas_db      │            │
│  └──────────┬───────────┘  └──────────┬───────────┘            │
│             │                          │                         │
│             │                          │                         │
│  ┌──────────┴──────────────────────────┴───────────┐            │
│  │         FACTURACIÓN (8085)                       │            │
│  │  • Gestión facturas                              │            │
│  │  • Estados y reportes                            │            │
│  │  • Integración con Tarifas (Feign)               │            │
│  │  DB: facturacion_db                              │            │
│  └──────────────────────────────────────────────────┘            │
│                                                                  │
└─────────────┬────────────────────────────────────────────────────┘
              │
              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    MySQL Server (3306)                           │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  • auth_db          - Autenticación                       │  │
│  │  • usuarios_db      - Usuarios y cuentas                  │  │
│  │  • tarifas_db       - Tarifas y precios                   │  │
│  │  • facturacion_db   - Facturas y reportes                 │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🔄 Flujo de una Request

```
1. Cliente → Gateway (8080)
   ├─ Autenticación JWT
   └─ Validación de token

2. Gateway → Eureka (8761)
   ├─ Consulta instancia del microservicio
   └─ Balanceo de carga

3. Gateway → Microservicio
   ├─ Enrutamiento por path
   │  • /api/usuarios/* → usuarios-microservice
   │  • /api/tarifas/*  → microservice-tarifas
   │  • /api/facturas/* → microservice-facturacion
   └─ Forward del request

4. Microservicio → Config Server (8081)
   └─ Carga configuración al iniciar

5. Microservicio → Base de Datos (3306)
   ├─ Operaciones CRUD
   └─ Transacciones

6. Microservicio → Otro Microservicio (Feign)
   └─ Comunicación inter-servicios
      Ejemplo: Facturación → Tarifas

7. Respuesta → Cliente
   └─ JSON response
```

---

## 📦 Resumen de Servicios

| #  | Servicio | Puerto | DB | Tipo | Responsabilidad |
|----|----------|--------|-----|------|-----------------|
| 1️⃣ | **Config Server** | 8081 | - | Infraestructura | Configuración centralizada |
| 2️⃣ | **Eureka Server** | 8761 | - | Infraestructura | Service Discovery |
| 3️⃣ | **Gateway** | 8080 | `auth_db` | Infraestructura | API Gateway + Auth |
| 4️⃣ | **Usuarios** | 8083 | `usuarios_db` | Negocio | Usuarios y Cuentas |
| 5️⃣ | **Tarifas** | 8084 | `tarifas_db` | Negocio | Tarifas y Precios |
| 6️⃣ | **Facturación** | 8085 | `facturacion_db` | Negocio | Facturas y Reportes |

---

## 🔐 Seguridad

### Autenticación JWT

```
1. Cliente envía credenciales
   POST /api/authenticate
   { "username": "user", "password": "pass" }

2. Gateway valida y genera token
   { "id_token": "eyJhbGci..." }

3. Cliente incluye token en requests
   Authorization: Bearer <token>

4. Gateway valida token en cada request
   ├─ Token válido → Forward a microservicio
   └─ Token inválido → 401 Unauthorized
```

### Roles y Permisos

- **ROLE_USER**: Acceso básico
- **ROLE_ADMIN**: Acceso administrativo

---

## 🔌 Comunicación Entre Microservicios

### Feign Clients

**Ejemplo: Facturación → Tarifas**

```java
@FeignClient(name = "microservice-tarifas")
public interface TarifaFeignClient {
    
    @GetMapping("/api/tarifas/activa")
    TarifaResponseDTO obtenerTarifaActiva();
    
    @GetMapping("/api/tarifas/vigente")
    TarifaResponseDTO obtenerTarifaVigenteEnFecha(@RequestParam LocalDate fecha);
}
```

**Ventajas**:
- ✅ Balanceo de carga automático (vía Eureka)
- ✅ Circuit breaker integrado
- ✅ Retry automático
- ✅ Timeout configurable

---

## 💾 Estrategia de Base de Datos

### Database per Service Pattern

Cada microservicio tiene su propia base de datos:

```
usuarios-microservice     →  usuarios_db
  ├─ Usuario
  ├─ Cuenta
  └─ UsuarioCuenta (join table)

microservice-tarifas      →  tarifas_db
  └─ Tarifa

microservice-facturacion  →  facturacion_db
  └─ Factura

gateway                   →  auth_db
  └─ User (autenticación)
```

**Ventajas**:
- ✅ Independencia de datos
- ✅ Escalabilidad por servicio
- ✅ Aislamiento de fallos
- ✅ Libertad tecnológica

**Consideraciones**:
- ⚠️ No joins entre microservicios
- ⚠️ Consistencia eventual
- ⚠️ Duplicación de datos si es necesaria

---

## 🚀 Escalabilidad

### Horizontal Scaling

```
                    Gateway (8080)
                         │
           ┌─────────────┼─────────────┐
           │             │             │
    Usuarios-1      Usuarios-2    Usuarios-3
     (8083)          (8084)        (8085)
```

**Cómo escalar**:
```bash
# Ejecutar múltiples instancias
java -jar usuarios-microservice.jar --server.port=8083
java -jar usuarios-microservice.jar --server.port=8093
java -jar usuarios-microservice.jar --server.port=8103

# Eureka balancea automáticamente
```

---

## 🔄 Resiliencia

### Circuit Breaker (Resilience4j)

```java
@CircuitBreaker(name = "tarifas-service", fallbackMethod = "obtenerTarifaDefault")
public TarifaResponseDTO obtenerTarifa() {
    return tarifaFeignClient.obtenerTarifaActiva();
}

public TarifaResponseDTO obtenerTarifaDefault(Exception e) {
    // Respuesta por defecto si el servicio falla
    return new TarifaResponseDTO(100.0, 50.0);
}
```

**Estados del Circuit Breaker**:
1. **CLOSED**: Funcionamiento normal
2. **OPEN**: Demasiados errores, no llamar al servicio
3. **HALF_OPEN**: Intentar recuperación

---

## 📊 Monitoreo

### Actuator Endpoints

Todos los microservicios exponen:

```
GET /actuator/health      - Estado del servicio
GET /actuator/info        - Información del servicio
GET /actuator/metrics     - Métricas del servicio
```

### Eureka Dashboard

```
http://localhost:8761
```

**Información disponible**:
- ✅ Servicios registrados
- ✅ Estado de salud
- ✅ Instancias disponibles
- ✅ Metadatos


