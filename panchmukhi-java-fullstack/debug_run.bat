@echo off
echo [1] Checking Docker Info...
docker info
if %errorlevel% neq 0 (
    echo Docker is NOT running.
    echo Attempting to start Docker Desktop...
    start "" "C:\Program Files\Docker\Docker\Docker Desktop.exe"
    echo Please wait for Docker to start, then run this script again.
    pause
    exit /b
)

echo.
echo [2] Starting Databases...
docker-compose up -d postgres mongodb redis

echo.
echo [3] Starting ML Services...
start "ML Services" cmd /k "cd ml-services && pip install -r requirements.txt && python -m uvicorn app:app --reload --port 8000"

echo.
echo [4] Starting Backend...
start "Backend" cmd /k "cd backend && set MAKEN_PROJECTBASEDIR=%cd% && mvnw spring-boot:run"

echo.
echo [5] Starting Frontend...
start "Frontend" cmd /k "cd frontend && python -m http.server 3000"

echo.
echo Done.
pause
