# 🚀 Microservicios

## 📋 Tabla de Contenidos

- [Arquitectura](#-arquitectura)
- [Características](#-características)
- [Prerrequisitos](#-prerrequisitos)
- [Instalación y Ejecución](#-instalación-y-ejecución)
- [Pruebas de la API](#-pruebas-de-la-api)
- [URLs de Servicios](#-urls-de-servicios)
- [Endpoints Disponibles](#-endpoints-disponibles)
- [Configuración](#-configuración)
- [Agregar Nuevos Microservicios](#-agregar-nuevos-microservicios)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)

## 🏗️ Arquitectura

El template incluye los siguientes servicios:

| Servicio | Puerto | Descripción |
|----------|--------|-------------|
| **Config Service** | 8081 | Gestión de configuración centralizada |
| **Eureka Service** | 8761 | Registro y descubrimiento de servicios |
| **Gateway** | 8080 | API Gateway con autenticación JWT |
| **Usuarios Microservice** | 8083 | Microservicio de usuarios y cuentas |
| **MySQL** | 3306 | Base de datos |

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

- **Java 17+**
- **Maven 3.6+**
- **Docker** (para MySQL)
- **Git**

## 🚀 Instalación y Ejecución

### Paso 1: Clonar el Repositorio

```bash
git clone <repository-url>
cd TP_ESPECIAL
```

### Paso 2: Configurar MySQL con Docker

```bash
# Detener cualquier MySQL existente
docker stop $(docker ps -q --filter "expose=3306") 2>/dev/null || true

# Ejecutar MySQL con configuración inicial
docker run -d --name microservices-mysql \
  -e MYSQL_ROOT_PASSWORD=password \
  -e MYSQL_DATABASE=auth_db \
  -p 3306:3306 \
  -v $(pwd)/init-scripts:/docker-entrypoint-initdb.d \
  mysql:8.0
```

### Paso 3: Compilar el Proyecto

```bash
mvn clean compile
```

### Paso 4: Ejecutar los Servicios

**IMPORTANTE**: Los servicios deben iniciarse en el siguiente orden:

#### 4.1 Config Service (Puerto 8081)
```bash
cd config-service
mvn spring-boot:run
```
Espera hasta ver: `Started ConfigServiceApplication`

#### 4.2 Eureka Service (Puerto 8761)
```bash
# En una nueva terminal
cd eureka-service
mvn spring-boot:run
```
Espera hasta ver: `Started EurekaServiceApplication`

#### 4.3 Gateway (Puerto 8080)
```bash
# En una nueva terminal
cd gateway
mvn spring-boot:run
```
Espera hasta ver: `Started GatewayApplication`

#### 4.4 Usuarios Microservice (Puerto 8083)
```bash
# En una nueva terminal
cd usuarios-microservice
mvn spring-boot:run
```
Espera hasta ver: `Started UsuariosMicroserviceApplication`

### Paso 5: Verificar que Todo Esté Funcionando

```bash
# Verificar servicios registrados en Eureka
curl http://localhost:8761/eureka/apps

# Verificar Config Service
curl http://localhost:8081/actuator/health
```

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

- **Eureka Dashboard**: http://localhost:8761
- **API Gateway**: http://localhost:8080
- **Config Service**: http://localhost:8081
- **Usuarios Microservice**: http://localhost:8083 (acceso directo)

## 📡 Endpoints Disponibles

### Autenticación (Gateway)
- `POST /api/authenticate` - Login y obtener JWT token
- `POST /api/usuarios` - Registrar nuevo usuario

### Usuarios Microservice (vía Gateway)
#### Usuarios
- `GET /api/usuarios` - Obtener todos los usuarios (paginado)
- `POST /api/usuarios` - Crear nuevo usuario
- `GET /api/usuarios/{id}` - Obtener usuario por ID
- `GET /api/usuarios/email/{email}` - Obtener usuario por email
- `GET /api/usuarios/celular/{celular}` - Obtener usuario por celular
- `PUT /api/usuarios/{id}` - Actualizar usuario
- `DELETE /api/usuarios/{id}` - Eliminar usuario
- `GET /api/usuarios/search?nombre={nombre}&apellido={apellido}` - Buscar usuarios por nombre
- `POST /api/usuarios/{usuarioId}/cuentas/{cuentaId}` - Asociar usuario a cuenta
- `DELETE /api/usuarios/{usuarioId}/cuentas/{cuentaId}` - Desasociar usuario de cuenta

#### Cuentas
- `GET /api/cuentas` - Obtener todas las cuentas (paginado)
- `POST /api/cuentas` - Crear nueva cuenta
- `GET /api/cuentas/{id}` - Obtener cuenta por ID
- `GET /api/cuentas/mercado-pago/{idMercadoPago}` - Obtener cuenta por ID Mercado Pago
- `GET /api/cuentas/estado/{estado}` - Obtener cuentas por estado
- `GET /api/cuentas/tipo/{tipo}` - Obtener cuentas por tipo (BASICA/PREMIUM)
- `PATCH /api/cuentas/{id}/habilitar` - Habilitar cuenta
- `PATCH /api/cuentas/{id}/deshabilitar` - Deshabilitar cuenta
- `PATCH /api/cuentas/{id}/cargar-saldo?monto={monto}` - Cargar saldo
- `PATCH /api/cuentas/{id}/descontar-saldo?monto={monto}` - Descontar saldo
- `DELETE /api/cuentas/{id}` - Eliminar cuenta

## ⚙️ Configuración

### Configuración de Base de Datos

Las configuraciones de base de datos se encuentran en:
- `config-service/src/main/resources/config-data/gateway.yml`
- `config-service/src/main/resources/config-data/sample-microservice.yml`

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/auth_db?createDatabaseIfNotExist=true
    username: root
    password: password
```

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
    <module>sample-microservice</module>
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

## 🐳 Ejecución con Docker

### Opción 1: Solo MySQL con Docker

```bash
# Ejecutar solo MySQL (recomendado para desarrollo)
docker run -d --name microservices-mysql \
  -e MYSQL_ROOT_PASSWORD=password \
  -e MYSQL_DATABASE=auth_db \
  -p 3306:3306 \
  -v $(pwd)/init-scripts:/docker-entrypoint-initdb.d \
  mysql:8.0

# Luego ejecutar servicios con Maven como se explicó arriba
```

### Opción 2: Todo con Docker Compose (Futuro)

```bash
# Nota: Requiere crear Dockerfiles para cada servicio
docker-compose up -d
```

## 🔍 Solución de Problemas

### Problema: Puerto ya en uso
```bash
# Verificar qué está usando el puerto
lsof -i :8080

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