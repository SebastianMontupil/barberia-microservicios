@echo off
setlocal enabledelayedexpansion

set "ROOT=%~dp0"
set "DRY_RUN="

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
    start "%SERVICE% - %PORT%" cmd /k "cd /d ""%SERVICE_DIR%"" && mvnw.cmd spring-boot:run"
    timeout /t 2 /nobreak >nul
)
exit /b 0
