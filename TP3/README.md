## 📚 Endpoints disponibles

### 🎓 Estudiantes (`/estudiantes`)

#### 1. Obtener todos los estudiantes
**Descripción:** Recupera todos los estudiantes ordenados por apellido

```bash
curl -X GET http://localhost:8080/estudiantes/ | jq
```

---

#### 2. Dar de alta un nuevo estudiante
**Descripción:** Crea un nuevo estudiante en el sistema.

```bash
curl -X POST http://localhost:8080/estudiantes \
  -H "Content-Type: application/json" \
  -d '{
    "dni": 99999999,
    "nombre": "Juan",
    "apellido": "Pérez",
    "edad": 22,
    "genero": "Male",
    "lu": 99999,
    "ciudad": "Tandil"
  }' | jq
```

**Respuestas:**
- `201 Created`: Estudiante creado exitosamente
- `409 Conflict`: DNI o LU ya existe

---

#### 3. Buscar estudiante por número de libreta universitaria (LU)
**Descripción:** Recupera un estudiante específico usando su número de libreta.

```bash
curl -X GET http://localhost:8080/estudiantes/lu/35056 | jq
```

**Ejemplo de respuesta exitosa:**
```json
{
  "dni": 24549124,
  "nombre": "Wenda",
  "apellido": "Jertz",
  "edad": 43,
  "genero": "Female",
  "lu": 35056,
  "ciudad": "Qiligang"
}

```

**Respuestas:**
- `200 OK`: Estudiante encontrado
- `404 Not Found`: No existe estudiante con ese LU

---

#### 4. Buscar estudiantes por género
**Descripción:** Recupera todos los estudiantes de un género específico, ordenados por apellido y nombre.

**Valores válidos para género:** `Male` o `Female`

```bash
curl -X GET http://localhost:8080/estudiantes/genero/Female | jq
```

---

### 📖 Carreras (`/carreras`)

#### 5. Obtener todas las carreras
**Descripción:** Recupera la lista completa de carreras disponibles.

```bash
curl -X GET http://localhost:8080/carreras/ | jq
```

---

#### 6. Obtener carreras con estudiantes inscriptos
**Descripción:** Retorna todas las carreras que tienen estudiantes inscriptos, ordenadas de mayor a menor según la cantidad de inscriptos.

```bash
curl -X GET "http://localhost:8080/carreras?inscriptos=true" | jq
```

**Ejemplo de respuesta:**
```json
[
  {
    "idCarrera": 1,
    "nombre": "Ingeniería en Sistemas",
    "duracion": 5,
    "cantidadInscriptos": 45
  },
  {
    "idCarrera": 2,
    "nombre": "Medicina",
    "duracion": 6,
    "cantidadInscriptos": 38
  }
]
```

---

#### 7. Generar reporte de carreras por año
**Descripción:** Genera un reporte completo que muestra para cada carrera la cantidad de inscriptos y egresados por año. Las carreras están ordenadas alfabéticamente y los años de manera cronológica.

```bash
curl -X GET http://localhost:8080/carreras/reporte | jq
```

**Ejemplo de respuesta:**
```json
[
  {
    "nombreCarrera": "Ingeniería en Sistemas",
    "idCarrera": 1,
    "anio": 2020,
    "inscriptos": 15,
    "egresados": 0
  },
  {
    "nombreCarrera": "Ingeniería en Sistemas",
    "idCarrera": 1,
    "anio": 2021,
    "inscriptos": 12,
    "egresados": 8
  }
]
```

---

### 🔗 Estudiantes y Carreras (Inscripciones)

#### 8. Inscribir estudiante en carrera
**Descripción:** Da de alta un estudiante en una carrera usando solo DNI del estudiante e ID de la carrera.

```bash
curl -X POST http://localhost:8080/inscripciones \
  -H "Content-Type: application/json" \
  -d '{
    "idEstudiante": 24549124,
    "idCarrera": 1
  }' | jq
```

**Respuestas:**
- `201 Created`: Estudiante inscrito exitosamente
- `404 Not Found`: Estudiante o carrera no encontrados
- `409 Conflict`: El estudiante ya está inscrito en esta carrera

---

#### 9. Obtener estudiantes de una carrera por ciudad
**Descripción:** Recupera todos los estudiantes inscritos en una carrera específica que residen en una ciudad determinada, ordenados por apellido y nombre.

**Parámetros de consulta:**
- `carrera`: ID de la carrera (opcional)
- `ciudad`: Ciudad de residencia (opcional)

```bash
# Filtrar por carrera y ciudad
curl -X GET "http://localhost:8080/inscripciones?carrera=1&ciudad=Paltamo" | jq
```

### Base de datos
La configuración de la base de datos se encuentra en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/DB_INTEGRADOR_TP3
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=create
```
