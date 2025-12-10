@echo off
setlocal enabledelayedexpansion

echo ===================================================
echo   Panchmukhi Trading Brain Pro - Startup Script
echo ===================================================

:: 1. Check Docker Installation
echo [1/4] Checking System Requirements...
docker --version >nul 2>&1
if %errorlevel% neq 0 goto docker_not_installed

:: 2. Check Docker Daemon Status
docker info >nul 2>&1
if %errorlevel% equ 0 goto docker_is_running

echo [WARN] Docker is not running. Attempting to start Docker Desktop...
if exist "C:\Program Files\Docker\Docker\Docker Desktop.exe" (
    start "" "C:\Program Files\Docker\Docker\Docker Desktop.exe"
) else (
    goto docker_not_found
)

echo Waiting for Docker to initialize (this may take a minute)...

:wait_loop
timeout /t 5 >nul
docker info >nul 2>&1
if %errorlevel% neq 0 (
    echo Still waiting for Docker...
    goto wait_loop
)
echo [OK] Docker started successfully.
goto start_services

:docker_is_running
echo [OK] Docker is running.
goto start_services

:docker_not_installed
echo [ERROR] Docker is not installed or not in PATH.
echo Please install Docker Desktop to run the databases.
pause
exit /b 1

:docker_not_found
echo [ERROR] Could not find Docker Desktop executable at default location.
echo Please start Docker Desktop manually and try again.
pause
exit /b 1

:start_services
:: 3. Start Databases (clean up existing containers first)
echo.
echo [2/4] Starting Databases (PostgreSQL, MongoDB, Redis)...
echo Cleaning up any existing containers...
docker-compose down >nul 2>&1
docker rm -f panchmukhi-postgres panchmukhi-mongodb panchmukhi-redis >nul 2>&1
docker-compose up -d postgres mongodb redis
if %errorlevel% neq 0 (
    echo [ERROR] Failed to start databases.
    pause
    exit /b 1
)
echo [OK] Databases are running.

:: 4. Start ML Services
echo.
echo [3/4] Starting ML Services (Python)...
start "ML Services" cmd /k "cd ml-services && echo Installing requirements... && pip install -r requirements.txt && echo Starting Server... && python -m uvicorn app:app --reload --port 8000"

:: 5. Start Backend
echo.
echo [4/4] Starting Backend (Spring Boot)...
echo This may take a few moments to build and start...
start "Backend" cmd /k "cd backend && set MAVEN_MULTIMODULEPROJECTDIRECTORY=%cd% && mvnw spring-boot:run"

:: 6. Start Frontend
echo.
echo [5/4] Starting Frontend...
echo Serving frontend on http://localhost:3000
start "Frontend" cmd /k "cd frontend && python -m http.server 3000"

echo.
echo ===================================================
echo   All Systems Go! 
echo   - ML Services: http://localhost:8000
echo   - Backend: http://localhost:8083
echo   - Frontend: http://localhost:3000
echo   - Swagger UI: http://localhost:8083/swagger-ui.html
echo ===================================================
echo.
echo Press any key to stop all services (containers will remain running)...
pause >nul
