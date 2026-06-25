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
if not defined DB_PASSWORD set "DB_PASSWORD="
:: Si tu MySQL local usa un usuario distinto de root, escríbelo aquí:
if not defined DB_USERNAME set "DB_USERNAME=root"
if not defined EUREKA_URL set "EUREKA_URL=http://localhost:8761/eureka/"
:: ===================================================

if /I "%~1"=="--dry-run" set "DRY_RUN=1"

echo ========================================
echo Barberia - Levantar microservicios
echo ========================================
echo.
echo Este script abre una ventana por microservicio y ejecuta:
echo mvnw.cmd spring-boot:run
echo.

call :run_service eureka-server 8761
timeout /t 5 /nobreak >nul
call :run_service auth 8081
call :run_service barberos 8082
call :run_service agendas 8083
call :run_service servicios 8084
call :run_service pagos 8085
call :run_service notificaciones 8086
call :run_service resenas 8087
call :run_service inventario 8088
call :run_service reportes 8089
call :run_service api-gateway 8080

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

if exist "%SERVICE_DIR%\mvnw.cmd" (set "MAVEN_EXEC=mvnw.cmd") else (set "MAVEN_EXEC=mvn")

if defined DRY_RUN (
    echo [DRY-RUN] %SERVICE% - puerto %PORT%
    echo          cd /d "%SERVICE_DIR%" ^&^& %MAVEN_EXEC% spring-boot:run
) else (
    echo Iniciando %SERVICE% en puerto %PORT%...
    start "%SERVICE% - %PORT%" cmd /k "cd /d ""%SERVICE_DIR%"" && set "SPRING_DATASOURCE_USERNAME=%DB_USERNAME%" && set "SPRING_DATASOURCE_PASSWORD=%DB_PASSWORD%" && set "EUREKA_URL=%EUREKA_URL%" && %MAVEN_EXEC% spring-boot:run"
    timeout /t 2 /nobreak >nul
)
exit /b 0
