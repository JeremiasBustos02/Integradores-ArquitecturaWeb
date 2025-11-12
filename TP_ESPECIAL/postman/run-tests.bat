@echo off
REM Script para ejecutar pruebas de Postman desde línea de comandos en Windows
REM Requiere: Newman (npm install -g newman)

echo ========================================
echo 🚀 Sistema de Monopatines - Test Runner
echo ========================================
echo.

REM Verificar que Newman esté instalado
where newman >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Error: Newman no está instalado
    echo    Instalar con: npm install -g newman
    echo.
    echo    Alternativamente, importar las colecciones en Postman UI
    exit /b 1
)

echo ✅ Newman encontrado
echo.

REM Variables
set COLLECTION=Sistema_Monopatines_Collection.json
set ENVIRONMENT=Sistema_Monopatines_Environment.json
set BASE_URL=%1
if "%BASE_URL%"=="" set BASE_URL=http://localhost:8080

echo 📦 Configuración:
echo    Colección: %COLLECTION%
echo    Environment: %ENVIRONMENT%
echo    Base URL: %BASE_URL%
echo.

REM Verificar archivos
if not exist "%COLLECTION%" (
    echo ❌ Error: No se encuentra %COLLECTION%
    exit /b 1
)

if not exist "%ENVIRONMENT%" (
    echo ❌ Error: No se encuentra %ENVIRONMENT%
    exit /b 1
)

echo 🔍 Verificando que el sistema esté corriendo...
curl -f -s "%BASE_URL%/actuator/health" >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ Gateway respondiendo en %BASE_URL%
) else (
    echo ⚠️  Advertencia: Gateway no responde en %BASE_URL%
    echo    Asegúrate de que el sistema esté corriendo con:
    echo    docker-compose up -d
    echo.
    set /p continuar="¿Continuar de todas formas? (s/n): "
    if /i not "%continuar%"=="s" exit /b 1
)

echo.
echo 🧪 Ejecutando pruebas...
echo ========================
echo.

REM Ejecutar colección
newman run "%COLLECTION%" ^
    -e "%ENVIRONMENT%" ^
    --timeout-request 10000 ^
    --color on ^
    --reporters cli,html ^
    --reporter-html-export newman-report.html

REM Verificar resultado
if %errorlevel% equ 0 (
    echo.
    echo ✅ ¡Todas las pruebas completadas!
    echo 📊 Reporte HTML generado: newman-report.html
) else (
    echo.
    echo ❌ Algunas pruebas fallaron
    echo    Ver detalles en el output anterior
    exit /b 1
)

echo.
echo 🎉 Test Runner completado

