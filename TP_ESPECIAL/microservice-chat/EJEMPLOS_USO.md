# Ejemplos de Uso - Microservicio de Chat con IA

## Peticiones de ejemplo con curl

### 1. Health Check
```bash
curl -X GET http://localhost:8088/api/chat/health
```

### 2. Consultar paradas disponibles
```bash
curl -X POST http://localhost:8088/api/chat \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": 1,
    "mensaje": "¿Cuáles son las paradas disponibles?"
  }'
```

### 3. Consultar mis viajes
```bash
curl -X POST http://localhost:8088/api/chat \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": 1,
    "mensaje": "Muéstrame mis viajes"
  }'
```

### 4. Pregunta sobre ubicación de paradas
```bash
curl -X POST http://localhost:8088/api/chat \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": 1,
    "mensaje": "¿Dónde puedo devolver un monopatín?"
  }'
```

### 5. Pregunta sobre estadísticas de viajes
```bash
curl -X POST http://localhost:8088/api/chat \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": 1,
    "mensaje": "¿Cuántos viajes he realizado?"
  }'
```

### 6. Consulta combinada
```bash
curl -X POST http://localhost:8088/api/chat \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": 1,
    "mensaje": "¿Hay alguna parada cerca de donde terminó mi último viaje?"
  }'
```

## Peticiones con Postman

### Configuración de entorno

Crear variables de entorno:
- `BASE_URL`: http://localhost:8088
- `USUARIO_ID`: 1

### Collection: Chat Service

#### Request: Health Check
```
GET {{BASE_URL}}/api/chat/health
```

#### Request: Chat - Listar Paradas
```
POST {{BASE_URL}}/api/chat
Content-Type: application/json

{
  "usuarioId": {{USUARIO_ID}},
  "mensaje": "¿Qué paradas hay disponibles?"
}
```

#### Request: Chat - Mis Viajes
```
POST {{BASE_URL}}/api/chat
Content-Type: application/json

{
  "usuarioId": {{USUARIO_ID}},
  "mensaje": "Dame información sobre mis viajes"
}
```

#### Request: Chat - Consulta Natural
```
POST {{BASE_URL}}/api/chat
Content-Type: application/json

{
  "usuarioId": {{USUARIO_ID}},
  "mensaje": "Necesito saber dónde están las estaciones de monopatines"
}
```

## Respuestas esperadas

### Respuesta exitosa - Paradas
```json
{
  "exito": true,
  "mensaje": "Hay 5 paradas disponibles en el sistema:\n\n1. **Parada Central** - Ubicada en Av. Principal 123\n2. **Parada Norte** - Ubicada en Calle Norte 456\n3. **Parada Sur** - Ubicada en Av. Sur 789\n4. **Parada Este** - Ubicada en Calle Este 321\n5. **Parada Oeste** - Ubicada en Av. Oeste 654\n\nPuedes retirar o devolver monopatines en cualquiera de estas ubicaciones.",
  "datos": null
}
```

### Respuesta exitosa - Viajes
```json
{
  "exito": true,
  "mensaje": "Has realizado 12 viajes en total. Aquí está el resumen:\n\n- Viaje más reciente: 15/03/2025 - 5.2 km\n- Viaje más largo: 18.7 km\n- Distancia total recorrida: 87.3 km\n- Tiempo total: 6 horas 23 minutos",
  "datos": null
}
```

### Respuesta de error - Usuario no premium
```json
{
  "exito": false,
  "mensaje": "Solo los usuarios premium pueden usar el servicio de chat con IA"
}
```

### Respuesta de error - Servicio no disponible
```json
{
  "exito": false,
  "mensaje": "Error al comunicarse con los servicios: Connection refused"
}
```

## Verificar que el usuario es premium

Antes de usar el chat, asegúrate de que el usuario tenga rol PREMIUM:

```bash
# Verificar usuario
curl -X GET http://localhost:8083/api/usuarios/1

# La respuesta debe incluir: "rol": "PREMIUM"
