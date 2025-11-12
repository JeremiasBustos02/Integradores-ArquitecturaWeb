# 🚀 Microservicios

## 📋 Tabla de Contenidos

- [Arquitectura](#-arquitectura)
- [Características](#-características)
- [Prerrequisitos](#-prerrequisitos)
- [Instalación y Ejecución](#-instalación-y-ejecución)
- [Colecciones Postman](#-colecciones-postman)
- [Pruebas de la API](#-pruebas-de-la-api)
- [URLs de Servicios](#-urls-de-servicios)
- [Endpoints Disponibles](#-endpoints-disponibles)
- [Configuración](#-configuración)
- [Agregar Nuevos Microservicios](#-agregar-nuevos-microservicios)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)

## 🏗️ Arquitectura

El sistema incluye los siguientes servicios:

| Servicio | Puerto | Base de Datos | Descripción |
|----------|--------|---------------|-------------|
| **Config Service** | 8081 | - | Gestión de configuración centralizada |
| **Eureka Service** | 8761 | - | Registro y descubrimiento de servicios |
| **Gateway** | 8080 | `auth_db` | API Gateway con autenticación JWT |
| **Microservice User** | 8083 | `usuarios_db` | Gestión de usuarios y cuentas |
| **Microservice Monopatin** | 8082 | `monopatin_db` | Gestión de monopatines y viajes |
| **Microservice Tarifas** | 8084 | `tarifas_db` | Gestión de tarifas y precios |
| **Microservice Facturación** | 8085 | `facturacion_db` | Gestión de facturación y reportes |
| **MySQL** | 3306 | - | Servidor de base de datos |

### 📊 Bases de Datos

El sistema utiliza múltiples bases de datos MySQL (una por microservicio):

- **auth_db**: Datos de autenticación (Gateway)
- **usuarios_db**: Usuarios, cuentas y relaciones
- **monopatin_db**: Monopatines, viajes y ubicaciones
- **tarifas_db**: Tarifas, precios y vigencias
- **facturacion_db**: Facturas, estados y reportes

> Todas las bases de datos se crean automáticamente con `createDatabaseIfNotExist=true`

## ✨ Características

- ✅ **Autenticación JWT** completa
- ✅ **Descubrimiento de Servicios** con Eureka
- ✅ **API Gateway** con enrutamiento
- ✅ **Configuración Centralizada** con Spring Cloud Config
- ✅ **Seguridad basada en roles** (ADMIN, USER)
- ✅ **Base de datos MySQL** integrada
- ✅ **Arquitectura de Microservicios** escalable
- ✅ **Docker** ready

## 📋 Prerrequisitos

Antes de ejecutar la aplicación, asegúrate de tener instalado:

- **Docker** y **Docker Compose** (recomendado - ejecuta todo el sistema)
- **Git**

**Opcional** (solo si quieres ejecutar sin Docker):
- **Java 21+**
- **Maven 3.6+**

## 🚀 Instalación y Ejecución

### 🐳 Método Recomendado: Docker Compose (Simple y Rápido)

Este es el método **más fácil y rápido** para ejecutar todo el sistema:

#### Paso 1: Clonar el Repositorio

```bash
git clone <repository-url>
cd TP_ESPECIAL
```

#### Paso 2: Ejecutar con Docker Compose

```bash
# Construir y ejecutar todos los servicios
docker-compose up --build -d

# Ver el estado de los contenedores
docker-compose ps

# Ver logs en tiempo real
docker-compose logs -f

# Ver logs de un servicio específico
docker-compose logs -f microservice-user
```

#### Paso 3: Esperar a que los Servicios Inicien

⏱️ **Tiempo aproximado**: 2-3 minutos

Los servicios se inician en el orden correcto automáticamente:
1. ✅ MySQL (con bases de datos e inicialización)
2. ✅ Config Service (puerto 8081)
3. ✅ Eureka Service (puerto 8761)
4. ✅ Gateway (puerto 8080)
5. ✅ Todos los microservicios (8082, 8083, 8084, 8085)

#### Paso 4: Verificar que Todo Esté Funcionando

```bash
# Verificar servicios registrados en Eureka
curl http://localhost:8761/eureka/apps

# Verificar Config Service
curl http://localhost:8081/actuator/health

# Verificar Gateway
curl http://localhost:8080/actuator/health
```

#### Comandos Útiles de Docker Compose

```bash
# Detener todos los servicios
docker-compose down

# Detener y eliminar volúmenes (limpieza completa)
docker-compose down -v

# Reconstruir un servicio específico
docker-compose up --build -d microservice-user

# Ver logs de todos los servicios
docker-compose logs

# Ver logs de los últimos 100 registros
docker-compose logs --tail=100
```

---

### 💻 Método Alternativo: Ejecución Manual (Sin Docker)

**Solo si NO quieres usar Docker** (requiere Java y Maven instalados):

<details>
<summary>Haz clic para expandir las instrucciones de ejecución manual</summary>

#### Paso 1: Configurar MySQL

```bash
# Opción 1: Con Docker (solo MySQL)
docker run -d --name microservices-mysql \
  -e MYSQL_ROOT_PASSWORD=password \
  -e MYSQL_DATABASE=auth_db \
  -p 3306:3306 \
  -v $(pwd)/init-scripts:/docker-entrypoint-initdb.d \
  mysql:8.0

# Opción 2: MySQL local (debe estar corriendo en puerto 3306)
```

#### Paso 2: Ejecutar los Servicios (en orden)

**IMPORTANTE**: Debes ejecutar en terminales separadas y en este orden:

1. **Config Service** (Puerto 8081)
```bash
cd config-service && mvn spring-boot:run
```

2. **Eureka Service** (Puerto 8761)
```bash
cd eureka-service && mvn spring-boot:run
```

3. **Gateway** (Puerto 8080)
```bash
cd gateway && mvn spring-boot:run
```

4. **Microservicios** (puertos 8082-8085)
```bash
# Terminal 1
cd microservice-monopatin && mvn spring-boot:run

# Terminal 2
cd microservice-user && mvn spring-boot:run

# Terminal 3
cd microservice-tarifas && mvn spring-boot:run

# Terminal 4
cd microservice-facturacion && mvn spring-boot:run
```

</details>

## 📮 Colecciones Postman

Se han creado **colecciones completas de Postman** para probar todas las funcionalidades del sistema.

### 📦 Archivos Disponibles

En la carpeta `/postman/`:

- **`Sistema_Monopatines_Collection.json`** - Colección con 38 requests **100% AUTOMATIZADOS**
- **`Sistema_Monopatines_Environment.json`** - Variables de entorno configuradas
- **`README_POSTMAN.md`** - Guía detallada de uso
- **`MEJORAS_AUTOMATIZACION.md`** - Explicación de la automatización

> **✨ Características Especiales**:
> - Los endpoints son accesibles **sin autenticación JWT**
> - Los IDs se capturan **automáticamente** con test scripts
> - Flujo de viajes **completamente automatizado** (sin copiar/pegar IDs)
> - Compatible con **Newman CLI** para ejecución automática

### 🚀 Quick Start

1. **Importar en Postman**:
   - Import → Seleccionar `Sistema_Monopatines_Collection.json`
   - Import → Seleccionar `Sistema_Monopatines_Environment.json`

2. **Activar Environment**:
   - Selector de environments (top-right) → "Sistema Monopatines - Local"

3. **Ejecutar Setup Inicial**:
   - Carpeta **"1. Setup Inicial"** → Ejecutar todos los requests en orden
   - Esto crea: tarifas, paradas, usuarios, cuentas, monopatines
   - ✅ **Los IDs se capturan automáticamente** en variables

4. **Probar Funcionalidades** (100% automatizadas):
   - Carpeta **"3. Funcionalidad A"** → Reporte de kilómetros
   - Carpeta **"4. Funcionalidad B"** → Anular cuentas
   - Carpeta **"5. Funcionalidad C"** → Monopatines con X viajes
   - Carpeta **"6. Funcionalidad D"** → Total facturado
   - Carpeta **"7. Funcionalidad F"** → Ajuste de precios
   - Carpeta **"8. Funcionalidad G"** → Monopatines cercanos
   - Carpeta **"9. Funcionalidad H"** → Uso por usuario

### 📊 Cobertura

La colección cubre **7 de 8** funcionalidades requeridas (87.5%):

| Funcionalidad | Estado | Carpeta |
|---------------|--------|---------|
| a. Reporte kilómetros (con/sin pausas) | ✅ | 3. Funcionalidad A |
| b. Anular cuentas | ✅ | 4. Funcionalidad B |
| c. Monopatines con X viajes | ✅ | 5. Funcionalidad C |
| d. Total facturado | ✅ | 6. Funcionalidad D |
| e. Usuarios top | ❌ NO IMPLEMENTADO | - |
| f. Ajuste de precios | ✅ | 7. Funcionalidad F |
| g. Monopatines cercanos | ✅ | 8. Funcionalidad G |
| h. Uso por usuario | ✅ | 9. Funcionalidad H |

> 📚 Ver `postman/README_POSTMAN.md` para instrucciones detalladas y casos de prueba

---

## 🧪 Pruebas de la API

### 1. Crear un Usuario

```bash
curl -X POST "http://localhost:8080/api/usuarios?password=testpass123" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "firstName": "Test",
    "lastName": "User",
    "email": "test@example.com"
  }'
```

**Respuesta esperada:**
```json
{
  "id": 1,
  "username": "testuser",
  "firstName": "Test",
  "lastName": "User",
  "email": "test@example.com",
  "activated": true,
  "authorities": ["ROLE_USER"]
}
```

### 2. Autenticarse y Obtener JWT Token

```bash
curl -X POST http://localhost:8080/api/authenticate \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "testpass123"
  }'
```

**Respuesta esperada:**
```json
{
  "id_token": "eyJhbGciOiJIUzUxMiJ9...",
  "username": "testuser"
}
```

### 3. Probar Acceso Sin Token (Debe Fallar)

```bash
curl -w "\nStatus: %{http_code}\n" http://localhost:8080/api/samples
```

**Respuesta esperada:** `401 Unauthorized`

### 4. Probar Acceso Con Token JWT

```bash
# Reemplaza YOUR_JWT_TOKEN con el token obtenido en el paso 2
curl -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  http://localhost:8080/api/samples
```

**Respuesta esperada:** `200 OK` con `[]`

### 5. Crear Datos de Prueba

```bash
curl -X POST \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sample Test 1",
    "description": "This is a test sample",
    "active": true
  }' \
  http://localhost:8080/api/samples
```

### 6. Verificar Datos Creados

```bash
curl -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  http://localhost:8080/api/samples
```

## 🌐 URLs de Servicios

| Servicio | URL | Acceso |
|----------|-----|--------|
| **Eureka Dashboard** | http://localhost:8761 | Dashboard de servicios |
| **API Gateway** | http://localhost:8080 | Punto de entrada principal |
| **Config Service** | http://localhost:8081 | Configuración centralizada |
| **Microservice User** | http://localhost:8083 | Acceso directo (dev) |
| **Microservice Monopatin** | http://localhost:8082 | Acceso directo (dev) |
| **Microservice Tarifas** | http://localhost:8084 | Acceso directo (dev) |
| **Microservice Facturación** | http://localhost:8085 | Acceso directo (dev) |

> **Nota**: En producción, todos los microservicios deben accederse **solo a través del Gateway** (puerto 8080)

## 📡 Endpoints Disponibles

### 🔐 Autenticación (Gateway)
- `POST /api/authenticate` - Login y obtener JWT token
- `POST /api/usuarios` - Registrar nuevo usuario

### 👥 Microservice User (vía Gateway)

#### Usuarios
- `GET /api/usuarios` - Obtener todos los usuarios (paginado)
- `POST /api/usuarios` - Crear nuevo usuario
- `GET /api/usuarios/{id}` - Obtener usuario por ID
- `GET /api/usuarios/{id}/cuentas` - Obtener todas las cuentas del usuario
- `GET /api/usuarios/email/{email}` - Obtener usuario por email
- `GET /api/usuarios/celular/{celular}` - Obtener usuario por celular
- `PUT /api/usuarios/{id}` - Actualizar usuario
- `DELETE /api/usuarios/{id}` - Eliminar usuario
- `GET /api/usuarios/search?nombre={nombre}&apellido={apellido}` - Buscar usuarios
- `POST /api/usuarios/{usuarioId}/cuentas/{cuentaId}` - Asociar usuario a cuenta
- `DELETE /api/usuarios/{usuarioId}/cuentas/{cuentaId}` - Desasociar usuario

#### Cuentas
- `GET /api/cuentas` - Obtener todas las cuentas (paginado)
- `POST /api/cuentas` - Crear nueva cuenta
- `GET /api/cuentas/{id}` - Obtener cuenta por ID
- `GET /api/cuentas/mercado-pago/{idMercadoPago}` - Cuenta por ID Mercado Pago
- `GET /api/cuentas/estado/{estado}` - Cuentas por estado
- `GET /api/cuentas/tipo/{tipo}` - Cuentas por tipo (BASICA/PREMIUM)
- `PATCH /api/cuentas/{id}/habilitar` - Habilitar cuenta
- `PATCH /api/cuentas/{id}/deshabilitar` - Deshabilitar cuenta
- `PATCH /api/cuentas/{id}/cargar-saldo?monto={monto}` - Cargar saldo
- `PATCH /api/cuentas/{id}/descontar-saldo?monto={monto}` - Descontar saldo
- `DELETE /api/cuentas/{id}` - Eliminar cuenta

### 🛴 Microservice Monopatin (vía Gateway)

#### CRUD de Monopatines
- `GET /api/monopatines` - Listar todos los monopatines
- `POST /api/monopatines` - Crear nuevo monopatín
- `GET /api/monopatines/{id}` - Obtener monopatín por ID
- `PUT /api/monopatines/{id}` - Actualizar monopatín
- `DELETE /api/monopatines/{id}` - Eliminar monopatín

#### Gestión de Viajes
- `POST /api/viajes` - Iniciar viaje
- `PUT /api/viajes/{id}/finalizar` - Finalizar viaje
- `GET /api/viajes` - Listar viajes
- `GET /api/viajes/{id}` - Obtener viaje por ID

> 📚 Para más detalles ver documentación específica del microservicio

### 💰 Microservice Tarifas

#### CRUD de Tarifas
- `POST /api/tarifas` - Crear nueva tarifa (incluye ajuste de precios)
- `GET /api/tarifas` - Listar todas las tarifas
- `GET /api/tarifas/{id}` - Obtener tarifa por ID
- `PUT /api/tarifas/{id}` - Actualizar tarifa
- `DELETE /api/tarifas/{id}` - Eliminar tarifa

#### Consultas Especiales
- `GET /api/tarifas/activa` - Obtener tarifa actualmente activa
- `GET /api/tarifas/vigente?fecha={fecha}` - Tarifa vigente en fecha específica
- `GET /api/tarifas/buscar?fechaInicio={fecha}&fechaFin={fecha}` - Buscar por rango

> **💡 Ajuste de Precios**: Para ajustar precios, crear nueva tarifa con `activa: true`

### 📄 Microservice Facturación

#### CRUD de Facturas
- `POST /api/facturas` - Crear nueva factura
- `GET /api/facturas` - Listar todas las facturas
- `GET /api/facturas/{id}` - Obtener factura por ID
- `PUT /api/facturas/{id}` - Actualizar factura
- `DELETE /api/facturas/{id}` - Eliminar factura
- `PATCH /api/facturas/{id}/estado?estado={estado}` - Cambiar estado

#### Consultas y Filtros
- `GET /api/facturas/cuenta/{cuentaId}` - Facturas por cuenta
- `GET /api/facturas/estado/{estado}` - Facturas por estado
- `GET /api/facturas/rango-fechas?fechaInicio={fecha}&fechaFin={fecha}` - Por rango
- `GET /api/facturas/cuenta/{cuentaId}/rango-fechas?...` - Cuenta + rango

#### Reportes
- `GET /api/facturas/reporte/total-facturado?mesInicio={mes}&mesFin={mes}&anio={año}` - Reporte financiero

**Estados válidos**: `PENDIENTE`, `PAGADA`, `VENCIDA`, `CANCELADA`

> 📚 Para más detalles ver [MICROSERVICES_README.md](./MICROSERVICES_README.md)

## ⚙️ Configuración

### Configuración de Base de Datos

Cada microservicio tiene su propia base de datos configurada en:
- `config-service/src/main/resources/config-data/`

| Microservicio | Archivo de Config | Base de Datos |
|---------------|-------------------|---------------|
| Gateway | `gateway.yml` / `gateway-docker.yml` | `auth_db` |
| User | `microservice-user.yml` / `microservice-user-docker.yml` | `usuarios_db` |
| Monopatin | `microservice-monopatin.yml` / `microservice-monopatin-docker.yml` | `monopatin_db` |
| Tarifas | `microservice-tarifas.yml` / `microservice-tarifas-docker.yml` | `tarifas_db` |
| Facturación | `microservice-facturacion.yml` / `microservice-facturacion-docker.yml` | `facturacion_db` |

> **Nota**: Los archivos `*-docker.yml` se usan cuando se ejecuta con Docker Compose

**Configuración típica**:
```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/{db_name}?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
    username: root
    password: password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    database: mysql
```

> **Nota**: Las bases de datos se crean automáticamente al iniciar cada microservicio

### Configuración JWT

El secreto JWT está configurado en `TokenProvider.java`. **En producción, usa variables de entorno:**

```java
private static final String SECRET = "j7ZookpUTYxclaULynjypGQVKMYXqOXMI+/1sQ2gOV1BF6VOHw6OzYj9RNZY4GcHAE3Igrah3MZ26oLrY/3y4Q==";
```

### Configuración de Seguridad

Las reglas de seguridad están definidas en `SecurityConfig.java`:

```java
.requestMatchers(HttpMethod.POST, "/api/authenticate").permitAll()
.requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()
.requestMatchers("/api/samples/**").hasAuthority(AuthorityConstant.USER)
```

## 🔧 Agregar Nuevos Microservicios

### 1. Crear Nuevo Módulo

```bash
# Copiar la estructura del sample-microservice
cp -r sample-microservice nuevo-microservice
```

### 2. Actualizar POM Parent

Agregar el nuevo módulo al `pom.xml` principal:

```xml
<modules>
    <module>config-service</module>
    <module>eureka-service</module>
    <module>gateway</module>
    <module>microservice-user</module>
    <module>microservice-monopatin</module>
    <module>microservice-tarifas</module>
    <module>microservice-facturacion</module>
    <module>nuevo-microservice</module>
</modules>
```

### 3. Crear Configuración

Crear `config-service/src/main/resources/config-data/nuevo-microservice.yml`:

```yaml
server:
  port: 8083

eureka:
  client:
    fetch-registry: true
    register-with-eureka: true
    service-url:
      default-zone: http://localhost:8761/eureka
  instance:
    hostname: localhost

spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/nuevo_db?createDatabaseIfNotExist=true
    username: root
    password: password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    database: mysql
```

### 4. Actualizar Gateway

Agregar ruta en `config-service/src/main/resources/config-data/gateway.yml`:

```yaml
spring:
  cloud:
    gateway:
      mvc:
        routes:
          - id: nuevo-microservice
            uri: lb://nuevo-microservice
            predicates:
              - Path=/api/nuevo/**
```

### 5. Configurar Seguridad

Actualizar `SecurityConfig.java`:

```java
.requestMatchers("/api/nuevo/**").hasAuthority(AuthorityConstant.USER)
```

## 🛠️ Tecnologías Utilizadas

- **Spring Boot** 3.3.2
- **Spring Cloud** 2023.0.3
- **Spring Security** (JWT)
- **Spring Data JPA**
- **MySQL** 8.0
- **Eureka** (Service Discovery)
- **Spring Cloud Config**
- **Spring Cloud Gateway MVC**
- **Maven**
- **Docker**

## 🐳 Docker Compose - Arquitectura Completa

### Servicios Incluidos

Docker Compose configura automáticamente todos los servicios necesarios:

| Servicio | Container Name | Puerto | Healthcheck |
|----------|----------------|--------|-------------|
| MySQL | `microservices-mysql` | 3306 | ✅ |
| Config Service | `config-service` | 8081 | ✅ |
| Eureka Service | `eureka-service` | 8761 | ✅ |
| Gateway | `gateway` | 8080 | ✅ |
| Microservice User | `microservice-user` | 8083 | ✅ |
| Microservice Monopatin | `microservice-monopatin` | 8082 | ✅ |
| Microservice Tarifas | `microservice-tarifas` | 8084 | ✅ |
| Microservice Facturación | `microservice-facturacion` | 8085 | ✅ |

### Características de Docker Compose

- ✅ **Orden de inicio automático**: Los servicios se inician en el orden correcto
- ✅ **Health checks**: Verifica que cada servicio esté listo antes de iniciar dependencias
- ✅ **Red aislada**: Todos los servicios en red `microservices-network`
- ✅ **Persistencia de datos**: Volumen para MySQL
- ✅ **Perfiles de Spring**: Automáticamente usa perfil `docker`
- ✅ **Variables de entorno**: Configuración centralizada

### Comandos Adicionales

```bash
# Reconstruir un servicio específico sin caché
docker-compose build --no-cache microservice-user

# Escalar un servicio (crear múltiples instancias)
docker-compose up -d --scale microservice-user=2

# Ver uso de recursos
docker stats

# Ejecutar comando en un contenedor
docker-compose exec microservice-user bash
```

> **Tiempo de inicio**: ~2-3 minutos para todos los servicios

## 🔍 Solución de Problemas

### Problema: Puerto ya en uso
```bash
# Verificar qué está usando el puerto (ejemplo con 8080)
lsof -i :8080

# Verificar todos los puertos del sistema
lsof -i :8080 && lsof -i :8081 && lsof -i :8083 && lsof -i :8084 && lsof -i :8085 && lsof -i :8761

# Detener proceso
kill -9 <PID>
```

### Problema: MySQL no conecta
```bash
# Verificar que MySQL esté corriendo
docker ps | grep mysql

# Ver logs de MySQL
docker logs microservices-mysql
```

### Problema: Servicios no se registran en Eureka
1. Verificar que Eureka esté corriendo en puerto 8761
2. Verificar que Config Service esté corriendo en puerto 8081
3. Revisar logs de los servicios
4. Esperar ~30 segundos para el registro automático
5. Verificar en http://localhost:8761 que los servicios aparezcan

### Problema: Error de conexión entre microservicios
1. Verificar que todos los servicios estén registrados en Eureka
2. Verificar que los nombres de servicios coincidan (case-sensitive)
3. Revisar configuración de Feign Clients
4. Verificar timeouts en configuración

### Problema: Base de datos no se crea
1. Verificar que MySQL esté corriendo
2. Verificar permisos del usuario root
3. Revisar propiedad `createDatabaseIfNotExist=true` en URL
4. Verificar logs de JPA/Hibernate

### Problema: JWT Token inválido
1. Verificar que el usuario existe en la base de datos
2. Verificar que el token no haya expirado (válido por 1 día)
3. Verificar que el secreto JWT sea el mismo en todos los servicios

## 📝 Usuarios Predefinidos

El sistema incluye usuarios predefinidos (ver `init-scripts/init.sql`):

| Usuario | Contraseña | Rol |
|---------|------------|-----|
| admin | admin123 | ROLE_ADMIN |
| user | user123 | ROLE_USER |

## 🧪 Ejemplos de Prueba de Microservicios

### Usuarios y Cuentas Microservice

```bash
# 1. Crear un usuario
curl -X POST http://localhost:8083/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan",
    "apellido": "Pérez",
    "celular": "1234567890",
    "email": "juan.perez@example.com"
  }'

# 2. Crear una cuenta
curl -X POST http://localhost:8083/api/cuentas \
  -H "Content-Type: application/json" \
  -d '{
    "idCuentaMercadoPago": 123456,
    "tipoCuenta": "PREMIUM",
    "saldo": 5000.00
  }'

# 3. Asociar usuario a cuenta
curl -X POST http://localhost:8083/api/usuarios/1/cuentas/1

# 4. Obtener todas las cuentas de un usuario
curl http://localhost:8083/api/usuarios/1/cuentas

# 5. Obtener usuarios de una cuenta
curl http://localhost:8083/api/usuarios/cuenta/1

# 6. Cargar saldo en una cuenta
curl -X PATCH "http://localhost:8083/api/cuentas/1/cargar-saldo?monto=1000.00"

# 7. Descontar saldo (usado al finalizar viaje)
curl -X PATCH "http://localhost:8083/api/cuentas/1/descontar-saldo?monto=250.50"
```

### Tarifas Microservice

```bash
# 1. Crear una tarifa inicial
curl -X POST http://localhost:8084/api/tarifas \
  -H "Content-Type: application/json" \
  -d '{
    "montoBase": 100.0,
    "montoExtra": 50.0,
    "fechaVigencia": "2025-01-01",
    "activa": true,
    "descripcion": "Tarifa inicial 2025"
  }'

# 2. Obtener tarifa activa
curl http://localhost:8084/api/tarifas/activa

# 3. Crear nueva tarifa (ajuste de precios)
curl -X POST http://localhost:8084/api/tarifas \
  -H "Content-Type: application/json" \
  -d '{
    "montoBase": 120.0,
    "montoExtra": 60.0,
    "fechaVigencia": "2025-06-01",
    "activa": true,
    "descripcion": "Ajuste de precios - Verano"
  }'

# 4. Listar todas las tarifas
curl http://localhost:8084/api/tarifas
```

### Facturación Microservice

```bash
# 1. Crear una factura
curl -X POST http://localhost:8085/api/facturas \
  -H "Content-Type: application/json" \
  -d '{
    "cuentaId": 1,
    "montoTotal": 250.0,
    "fechaEmision": "2025-01-15T10:30:00",
    "fechaVencimiento": "2025-02-15",
    "estado": "PENDIENTE",
    "descripcion": "Viaje del 15/01",
    "periodoMes": 1,
    "periodoAnio": 2025,
    "tipoCuenta": "BASICA"
  }'

# 2. Consultar facturas por cuenta
curl http://localhost:8085/api/facturas/cuenta/1

# 3. Cambiar estado de factura
curl -X PATCH "http://localhost:8085/api/facturas/1/estado?estado=PAGADA"

# 4. Reporte de facturación
curl "http://localhost:8085/api/facturas/reporte/total-facturado?mesInicio=1&mesFin=3&anio=2025"

# 5. Facturas por estado
curl http://localhost:8085/api/facturas/estado/PENDIENTE
```

### Flujo Completo de Prueba

```bash
# 1. Crear tarifa
curl -X POST http://localhost:8084/api/tarifas \
  -H "Content-Type: application/json" \
  -d '{"montoBase": 100.0, "montoExtra": 50.0, "fechaVigencia": "2025-01-01", "activa": true}'

# 2. Verificar tarifa activa
curl http://localhost:8084/api/tarifas/activa

# 3. Crear cuenta (debe existir previamente en usuarios-microservice)
# curl -X POST http://localhost:8083/api/cuentas ...

# 4. Crear factura asociada a cuenta
curl -X POST http://localhost:8085/api/facturas \
  -H "Content-Type: application/json" \
  -d '{"cuentaId": 1, "montoTotal": 250.0, "fechaEmision": "2025-01-15T10:30:00", "estado": "PENDIENTE", "periodoMes": 1, "periodoAnio": 2025}'

# 5. Consultar facturas de la cuenta
curl http://localhost:8085/api/facturas/cuenta/1

# 6. Generar reporte de facturación
curl "http://localhost:8085/api/facturas/reporte/total-facturado?mesInicio=1&mesFin=12&anio=2025"
```

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE.md](LICENSE.md) para detalles.

---

**¡Happy Coding! 🚀**