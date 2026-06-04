@echo off
setlocal enabledelayedexpansion

:: ===================================================
:: CONFIGURACIÓN DE JAVA
:: Se detectó JDK 21 instalado en esta ruta. Se configura para evitar errores de incompatibilidad de versión de Java:
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot"
set "PATH=%JAVA_HOME%\bin;%PATH%"
:: ===================================================

set "ROOT=%~dp0"
set "DRY_RUN="

:: ===================================================
:: CONFIGURACIÓN DE CONEXIÓN A BASE DE DATOS
:: Si tu MySQL local usa contraseña, escríbela aquí:
set "DB_PASSWORD=123"
:: Si tu MySQL local usa un usuario distinto de root, escríbelo aquí:
set "DB_USERNAME=root"
:: ===================================================

if /I "%~1"=="--dry-run" set "DRY_RUN=1"

echo ========================================
echo Barberia - Levantar microservicios
echo ========================================
echo.
echo Este script abre una ventana por microservicio y ejecuta:
echo mvnw.cmd spring-boot:run
echo.

call :run_service auth 8081
call :run_service barberos 8082
call :run_service agendas 8083
call :run_service servicios 8084
call :run_service pagos 8085
call :run_service notificaciones 8086
call :run_service resenas 8087
call :run_service inventario 8088
call :run_service reportes 8089

echo.
if defined DRY_RUN (
    echo Dry run finalizado. No se abrieron ventanas.
) else (
    echo Microservicios iniciandose en ventanas separadas.
    echo Revisa cada consola para ver el estado de arranque.
)
echo.
pause
exit /b 0

:run_service
set "SERVICE=%~1"
set "PORT=%~2"
set "SERVICE_DIR=%ROOT%%SERVICE%"

if not exist "%SERVICE_DIR%\mvnw.cmd" (
    echo [ERROR] No se encontro "%SERVICE_DIR%\mvnw.cmd".
    exit /b 1
)

if defined DRY_RUN (
    echo [DRY-RUN] %SERVICE% - puerto %PORT%
    echo          cd /d "%SERVICE_DIR%" ^&^& mvnw.cmd spring-boot:run
) else (
    echo Iniciando %SERVICE% en puerto %PORT%...
    start "%SERVICE% - %PORT%" cmd /k "cd /d ""%SERVICE_DIR%"" && set "SPRING_DATASOURCE_USERNAME=%DB_USERNAME%" && set "SPRING_DATASOURCE_PASSWORD=%DB_PASSWORD%" && mvnw.cmd spring-boot:run"
    timeout /t 2 /nobreak >nul
)
exit /b 0
