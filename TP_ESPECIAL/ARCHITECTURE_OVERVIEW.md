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
│  │  USUARIOS (8083)     │  │  MONOPATINES (8082)  │            │
│  │  • Gestión usuarios  │  │  • Gestión flota     │            │
│  │  • Gestión cuentas   │  │  • GPS y ubicación   │            │
│  │  • Cuentas Premium   │  │  • Mantenimiento     │            │
│  │  • Usuarios frecuen. │  │  • Reportes uso      │            │
│  │  DB: usuarios_db     │  │  DB: monopatin_db    │            │
│  └──────────────────────┘  └──────────────────────┘            │
│                                                                  │
│  ┌──────────────────────────────────────────────┐               │
│  │         VIAJES (8086) - MongoDB              │               │
│  │  • Gestión de viajes                         │               │
│  │  • Validación GPS 🛰️ (50m tolerancia)       │               │
│  │  • Control de pausas y recargos             │               │
│  │  • Lógica Premium ⭐ (gratis/50% desc.)     │               │
│  │  • Facturación automática 💰                │               │
│  │  DB: viaje_db (NoSQL)                        │               │
│  └──────────────────────────────────────────────┘               │
│                                                                  │
│  ┌──────────────────────┐  ┌──────────────────────┐            │
│  │  TARIFAS (8084)      │  │  FACTURACIÓN (8085)  │            │
│  │  • Gestión tarifas   │  │  • Gestión facturas  │            │
│  │  • Precios vigentes  │  │  • Pago automático   │            │
│  │  • Ajustes program.🔄│  │  • Reportes finan.   │            │
│  │  DB: tarifas_db      │  │  DB: facturacion_db  │            │
│  └──────────────────────┘  └──────────────────────┘            │
│                                                                  │
│  ┌──────────────────────┐                                       │
│  │  PARADAS (8087)      │                                       │
│  │  • Gestión paradas   │                                       │
│  │  • Ubicaciones GPS   │                                       │
│  │  DB: parada_db       │                                       │
│  └──────────────────────┘                                       │
│                                                                  │
└─────────────┬────────────────────────────────────────────────────┘
              │
      ┌───────┴────────┐
      ▼                ▼
┌─────────────────┐  ┌─────────────────────────┐
│ MySQL (3306)    │  │  MongoDB (27017)        │
│  • auth_db      │  │  • viaje_db             │
│  • usuarios_db  │  │                         │
│  • monopatin_db │  └─────────────────────────┘
│  • tarifas_db   │
│  • facturacion  │
│  • parada_db    │
└─────────────────┘
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
   │  • /api/usuarios/*    → microservice-user
   │  • /api/cuentas/*     → microservice-user
   │  • /api/monopatines/* → microservice-monopatin
   │  • /api/viajes/*      → microservice-viajes
   │  • /api/paradas/*     → microservice-parada
   │  • /api/tarifas/*     → microservice-tarifas
   │  • /api/facturas/*    → microservice-facturacion
   └─ Forward del request

4. Microservicio → Config Server (8081)
   └─ Carga configuración al iniciar

5. Microservicio → Base de Datos
   ├─ MySQL (3306) para datos relacionales
   ├─ MongoDB (27017) para viajes y datos temporales
   ├─ Operaciones CRUD
   └─ Transacciones

6. Microservicio → Otro Microservicio (Feign)
   └─ Comunicación inter-servicios
      Ejemplos:
      • Viajes → Usuarios (validar cuenta)
      • Viajes → Monopatines (GPS, estado)
      • Viajes → Facturación (crear factura)
      • Facturación → Usuarios (descontar saldo)

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
| 4️⃣ | **Usuarios** | 8083 | `usuarios_db` | Negocio | Usuarios, Cuentas, Premium |
| 5️⃣ | **Monopatines** | 8082 | `monopatin_db` | Negocio | Flota, GPS, Mantenimiento |
| 6️⃣ | **Viajes** | 8086 | `viaje_db` (MongoDB) | Negocio | Viajes, GPS, Facturación |
| 7️⃣ | **Paradas** | 8087 | `parada_db` | Negocio | Paradas y ubicaciones |
| 8️⃣ | **Tarifas** | 8084 | `tarifas_db` | Negocio | Tarifas y Precios |
| 9️⃣ | **Facturación** | 8085 | `facturacion_db` | Negocio | Facturas y Reportes |

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

- **ROLE_USER**: Acceso básico (gestión de cuentas, viajes)
- **ROLE_ADMIN**: Acceso administrativo (reportes, gestión de flota)

---

## 🔌 Comunicación Entre Microservicios

### Feign Clients

**Ejemplo 1: Viajes → Usuarios**

```java
@FeignClient(name = "microservice-user")
public interface UsuarioClientRest {
    
    @GetMapping("/api/usuarios/{usuarioId}/cuentas/cuenta-para-facturar")
    CuentaResponseDTO obtenerCuentaParaFacturar(@PathVariable Long usuarioId);
    
    @GetMapping("/api/cuentas/{id}")
    CuentaResponseDTO getCuentaById(@PathVariable("id") Long cuentaId);
    
    @PostMapping("/api/cuentas/{id}/kilometros")
    CuentaResponseDTO actualizarKilometros(
        @PathVariable("id") Long cuentaId, 
        @RequestParam("kilometros") Double kilometros
    );
}
```

**Ejemplo 2: Viajes → Monopatines**

```java
@FeignClient(name = "microservice-monopatin")
public interface MonopatinClientRest {
    
    @GetMapping("/api/monopatines/{id}")
    MonopatinDTO getMonopatinById(@PathVariable("id") Long id);
    
    @PostMapping("/api/monopatines/{id}/estado")
    void actualizarEstado(
        @PathVariable("id") Long id,
        @RequestParam("estado") EstadoMonopatin estado
    );
}
```

**Ejemplo 3: Facturación → Usuarios (Pago Automático)**

```java
@FeignClient(name = "microservice-user")
public interface CuentaFeignClient {
    
    @PostMapping("/api/cuentas/{id}/descontar-saldo")
    CuentaResponseDTO descontarSaldo(
        @PathVariable("id") Long id,
        @RequestParam("monto") BigDecimal monto
    );
}
```

**Ventajas**:
- ✅ Balanceo de carga automático (vía Eureka)
- ✅ Circuit breaker integrado
- ✅ Retry automático
- ✅ Timeout configurable
- ✅ Descubrimiento dinámico de servicios

---

## 💾 Estrategia de Base de Datos

### Database per Service Pattern

Cada microservicio tiene su propia base de datos:

```
microservice-user           →  usuarios_db (MySQL)
  ├─ Usuario
  ├─ Cuenta
  └─ UsuarioCuenta (join table)

microservice-monopatin      →  monopatin_db (MySQL)
  ├─ Monopatin
  └─ Mantenimiento

microservice-viajes         →  viaje_db (MongoDB)
  ├─ Viaje
  └─ Pausa

microservice-parada         →  parada_db (MySQL)
  └─ Parada

microservice-tarifas        →  tarifas_db (MySQL)
  ├─ Tarifa
  ├─ PrecioVigente
  └─ AjustePrecioProgramado

microservice-facturacion    →  facturacion_db (MySQL)
  └─ Factura

gateway                     →  auth_db (MySQL)
  └─ User (autenticación)
```

**Ventajas**:
- ✅ Independencia de datos
- ✅ Escalabilidad por servicio
- ✅ Aislamiento de fallos
- ✅ Libertad tecnológica (MySQL + MongoDB)
- ✅ Schemas independientes

**Consideraciones**:
- ⚠️ No joins entre microservicios
- ⚠️ Consistencia eventual
- ⚠️ Comunicación por Feign cuando se necesitan datos de otros servicios

---

## ✨ Funcionalidades Avanzadas

### 1. Validación GPS 🛰️

**Ubicación:** `microservice-viajes`

Al finalizar un viaje, el sistema valida que el monopatín esté dentro de 50 metros de la parada de destino usando la fórmula de Haversine:

```java
private double calcularDistanciaGPS(double lat1, double lon1, double lat2, double lon2) {
    final int R = 6371000; // Radio de la Tierra en metros
    
    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);
    
    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
               Math.sin(dLon / 2) * Math.sin(dLon / 2);
    
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    
    return R * c; // Distancia en metros
}
```

Si la distancia > 50m, se rechaza la finalización del viaje.

---

### 2. Lógica de Cuentas Premium ⭐

**Ubicación:** `microservice-viajes` (ViajeService)

```
Cuenta Premium:
  • Primeros 100 km del mes: GRATIS 🆓
  • Kilómetros adicionales: 50% descuento 💰
  • Renovación automática mensual
```

**Implementación:**
1. Al finalizar viaje, verificar tipo de cuenta
2. Consultar kilómetros acumulados del mes
3. Si < 100km → Costo = $0
4. Si > 100km → Costo = Costo_Normal * 0.5
5. Actualizar kilómetros acumulados vía FeignClient

---

### 3. Pago Automático 💰

**Ubicación:** `microservice-facturacion` (FacturaService)

Al crear una factura:
1. Calcular monto total
2. Llamar a `microservice-user` vía Feign
3. Descontar saldo automáticamente
4. Si saldo insuficiente → Error con mensaje claro
5. Registrar factura con estado `PAGADA`

```java
// Descontar saldo automáticamente
CuentaResponseDTO cuentaActualizada = cuentaFeignClient.descontarSaldo(
    facturaRequest.getCuentaId(),
    BigDecimal.valueOf(facturaRequest.getMontoTotal())
);
```

---

### 4. Ajustes de Precio Programados 🔄

**Ubicación:** `microservice-tarifas` (Scheduler)

```java
@Scheduled(fixedDelay = 60000) // Cada 1 minuto
public void procesarAjustesProgramados() {
    LocalDateTime ahora = LocalDateTime.now();
    
    List<AjustePrecioProgramado> ajustesPendientes = 
        repository.findByEstadoAndFechaAplicacionBefore(
            EstadoAjuste.PENDIENTE, ahora
        );
    
    for (AjustePrecioProgramado ajuste : ajustesPendientes) {
        aplicarAjuste(ajuste);
        ajuste.setEstado(EstadoAjuste.APLICADO);
        repository.save(ajuste);
    }
}
```

**Uso:**
```json
POST /api/tarifas/precios/ajustes
{
  "precioBase": 150.0,
  "precioExtra": 80.0,
  "fechaAplicacion": "2025-12-01T00:00:00"
}
```

---

### 5. Detección de Pausas Extensas ⏱️

**Ubicación:** `microservice-viajes`

Si una pausa supera los 15 minutos:
- Aplicar tarifa extra al resto del viaje
- Registrar en la pausa como "extensa"
- Informar al usuario del cargo adicional

```java
if (duracionPausaMinutos > 15) {
    tarifaPorMinuto = tarifa.getPrecioPausaExtra();
    log.info("Pausa extensa detectada ({}min). Aplicando tarifa extra", duracionPausaMinutos);
}
```

---

## 🚀 Escalabilidad

### Horizontal Scaling

```
                    Gateway (8080)
                         │
           ┌─────────────┼─────────────┐
           │             │             │
    Usuarios-1      Usuarios-2    Usuarios-3
     (8083)          (8093)        (8103)
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

---

## 📈 Reportes Administrativos

### 1. Usuarios Frecuentes
**Endpoint:** `GET /api/usuarios/reporte/usuarios-frecuentes`
**Servicio:** `microservice-user`

Retorna los usuarios que más usan el sistema, ordenados por cantidad de viajes y kilómetros recorridos.

### 2. Monopatines por Uso
**Endpoint:** `GET /api/monopatines/reporte/kilometros?incluirPausas=true`
**Servicio:** `microservice-monopatin`

Reporte de uso de cada monopatín por kilómetros, con opción de incluir o excluir pausas.

### 3. Total Facturado
**Endpoint:** `GET /api/facturas/reporte/total-facturado?anio=2025&mesInicio=1&mesFin=12`
**Servicio:** `microservice-facturacion`

Reporte financiero del total facturado en un rango de meses.

---

## 🎯 Principios de Diseño Aplicados

1. **Single Responsibility**: Cada microservicio tiene una responsabilidad clara
2. **Database per Service**: Aislamiento de datos
3. **API Gateway Pattern**: Punto de entrada único
4. **Service Discovery**: Descubrimiento dinámico con Eureka
5. **Externalized Configuration**: Config Server centralizado
6. **Circuit Breaker**: Resiliencia ante fallos
7. **RESTful APIs**: Endpoints claros y semánticos
8. **Transactional Consistency**: `@Transactional` para operaciones críticas
9. **Input Validation**: Bean Validation en todos los DTOs
10. **Separation of Concerns**: DTOs separados para Request/Response

---

## 🔧 Tecnologías Utilizadas

| Categoría | Tecnología | Propósito |
|-----------|-----------|-----------|
| **Framework** | Spring Boot 3.2.0 | Base de microservicios |
| **Service Discovery** | Eureka | Registro y descubrimiento |
| **API Gateway** | Spring Cloud Gateway | Enrutamiento y seguridad |
| **Config Management** | Spring Cloud Config | Configuración centralizada |
| **Communication** | OpenFeign | Comunicación inter-servicios |
| **Security** | Spring Security + JWT | Autenticación y autorización |
| **Database (SQL)** | MySQL 8.0 | Persistencia relacional |
| **Database (NoSQL)** | MongoDB | Datos de viajes |
| **ORM** | Spring Data JPA | Mapeo objeto-relacional |
| **Build** | Maven | Gestión de dependencias |
| **Containerization** | Docker + Docker Compose | Orquestación de servicios |
| **Validation** | Bean Validation (Hibernate) | Validación de datos |
| **Logging** | SLF4J + Logback | Registro de eventos |
| **Scheduling** | Spring Scheduled | Tareas programadas |

---

## 📚 Documentación Adicional

- **README.md**: Guía de inicio rápido
- **postman/**: Colección completa de endpoints con ejemplos
- **docker-compose.yml**: Configuración de orquestación
- **init-scripts/**: Scripts de inicialización de bases de datos

---

**Versión:** 2.0  
**Última actualización:** Noviembre 2025  
**Estado:** Producción Ready ✅
