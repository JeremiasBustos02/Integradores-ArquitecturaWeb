# 📮 Guía de Uso - Colecciones Postman

## 📋 Contenido

- `Sistema_Monopatines_Collection.json` - Colección completa con todos los endpoints
- `Sistema_Monopatines_Environment.json` - Variables de entorno para facilitar las pruebas
- `README_POSTMAN.md` - Esta guía
- `run-tests.sh` - Script para ejecutar pruebas desde terminal (Linux/Mac)
- `run-tests.bat` - Script para ejecutar pruebas desde terminal (Windows)

---

## 💻 Ejecución desde Línea de Comandos (Opcional)

Si tienes **Newman** instalado (CLI de Postman), puedes ejecutar las pruebas desde terminal:

### Instalar Newman

```bash
# NPM
npm install -g newman

# Verificar instalación
newman --version
```

### Ejecutar Pruebas

**Linux/Mac**:
```bash
cd postman
./run-tests.sh
```

**Windows**:
```cmd
cd postman
run-tests.bat
```

**Con URL personalizada**:
```bash
./run-tests.sh http://mi-servidor:8080
```

**Resultado**: 
- ✅ Ejecuta toda la colección automáticamente
- ✅ Genera reporte HTML (`newman-report.html`)
- ✅ Muestra resultados en terminal

---

## 🚀 Cómo Importar en Postman

### Paso 1: Importar la Colección

### Paso 2: Importar el Environment

### Paso 3: Activar el Environment*


## 🔄 Resetear el Sistema

Si quieres empezar de cero:

```bash
# Opción 1: Bajar y subir Docker Compose
docker-compose down -v
docker-compose up -d

# Opción 2: Limpiar solo las bases de datos (mantener servicios)
# (depende de tu configuración)
```

Luego ejecutar nuevamente el **"1. Setup Inicial"**.

