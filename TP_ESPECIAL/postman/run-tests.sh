#!/bin/bash

# Script para ejecutar pruebas de Postman desde línea de comandos
# Requiere: Newman (npm install -g newman)

echo "🚀 Sistema de Monopatines - Test Runner"
echo "========================================"
echo ""

# Verificar que Newman esté instalado
if ! command -v newman &> /dev/null; then
    echo "❌ Error: Newman no está instalado"
    echo "   Instalar con: npm install -g newman"
    echo ""
    echo "   Alternativamente, importar las colecciones en Postman UI"
    exit 1
fi

echo "✅ Newman encontrado"
echo ""

# Variables
COLLECTION="Sistema_Monopatines_Collection.json"
ENVIRONMENT="Sistema_Monopatines_Environment.json"
BASE_URL=${1:-"http://localhost:8080"}

echo "📦 Configuración:"
echo "   Colección: $COLLECTION"
echo "   Environment: $ENVIRONMENT"
echo "   Base URL: $BASE_URL"
echo ""

# Verificar que los archivos existan
if [ ! -f "$COLLECTION" ]; then
    echo "❌ Error: No se encuentra $COLLECTION"
    exit 1
fi

if [ ! -f "$ENVIRONMENT" ]; then
    echo "❌ Error: No se encuentra $ENVIRONMENT"
    exit 1
fi

# Verificar que el sistema esté corriendo
echo "🔍 Verificando que el sistema esté corriendo..."
if curl -f -s "$BASE_URL/actuator/health" > /dev/null 2>&1; then
    echo "✅ Gateway respondiendo en $BASE_URL"
else
    echo "⚠️  Advertencia: Gateway no responde en $BASE_URL"
    echo "   Asegúrate de que el sistema esté corriendo con:"
    echo "   docker-compose up -d"
    echo ""
    read -p "¿Continuar de todas formas? (y/n): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

echo ""
echo "🧪 Ejecutando pruebas..."
echo "========================"
echo ""

# Ejecutar colección completa
newman run "$COLLECTION" \
    -e "$ENVIRONMENT" \
    --timeout-request 10000 \
    --color on \
    --reporters cli,html \
    --reporter-html-export newman-report.html

# Verificar resultado
if [ $? -eq 0 ]; then
    echo ""
    echo "✅ ¡Todas las pruebas completadas!"
    echo "📊 Reporte HTML generado: newman-report.html"
else
    echo ""
    echo "❌ Algunas pruebas fallaron"
    echo "   Ver detalles en el output anterior"
    exit 1
fi

echo ""
echo "🎉 Test Runner completado"

