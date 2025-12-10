@echo off
chcp 65001 >nul
echo ========================================
echo   Panchmukhi Trading - Complete Startup
echo ========================================
echo.

cd /d "%~dp0"

echo Starting Complete Project...
echo.

echo [1/2] Starting Backend Server...
echo.
start "Backend Server" cmd /k "cd /d %~dp0 && start-backend.bat"

echo ✅ Backend server starting in new window...
echo.
echo Waiting 10 seconds for backend to initialize...
timeout /t 10 >nul

echo.
echo [2/2] Opening Frontend...
echo.

cd panchmukhi-java-fullstack\frontend

start index.html
timeout /t 1 >nul
start news.html
timeout /t 1 >nul
start sectors.html
timeout /t 1 >nul
start scraper-admin.html

echo.
echo ========================================
echo   ✅ Project Started Successfully!
echo ========================================
echo.
echo Backend: http://localhost:8083
echo Frontend: Opened in browser
echo.
echo Press any key to close this window...
echo (Backend will keep running in separate window)
echo.
pause >nul
