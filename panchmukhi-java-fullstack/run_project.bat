@echo off
echo ===================================================
echo   Panchmukhi Trading Brain Pro - Startup Script
echo ===================================================

echo [1/3] Starting ML Services (Python)...
echo Installing dependencies...
start "ML Services" cmd /k "cd ml-services && pip install -r requirements.txt && python -m uvicorn app:app --reload --port 8000"

echo [2/3] Starting Backend (Spring Boot)...
echo This may take a few moments to build and start...
start "Backend" cmd /k "cd backend && set MAKEN_PROJECTBASEDIR=%cd% && mvnw spring-boot:run"

echo [3/3] Waiting for services to initialize...
timeout /t 15

echo Opening Frontend Dashboard...
start frontend/index.html

echo ===================================================
echo   All Systems Go! 
echo   - ML Services: http://localhost:8000
echo   - Backend: http://localhost:8080
echo   - Frontend: Opened in Browser
echo ===================================================
pause
