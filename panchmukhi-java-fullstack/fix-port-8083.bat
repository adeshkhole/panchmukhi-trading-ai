@echo off
echo ====================================================
echo   Panchmukhi Trading - Fix Port 8083
echo ====================================================
echo.

echo [Step 1/2] Syncing Frontend files to Backend...
echo.

:: Create static directory if needed
if not exist "backend\src\main\resources\static" (
    echo Creating static directory...
    mkdir "backend\src\main\resources\static"
)

:: Copy all frontend files
echo Copying files...
robocopy "frontend" "backend\src\main\resources\static" /E /NFL /NDL /NJH /NJS /R:2 /W:1

if %errorlevel% LEQ 3 (
    echo [OK] Files synced successfully!
) else (
    echo [ERROR] Failed to sync. Error: %errorlevel%
    pause
    exit /b 1
)

echo.
echo [Step 2/2] Restarting Backend...
echo.

:: Find and kill existing Java process on port 8083
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8083') do (
    echo Stopping existing backend (PID: %%a)...
    taskkill /F /PID %%a >nul 2>&1
)

:: Wait a bit
timeout /t 2 /nobreak >nul

:: Start backend in new window
echo Starting backend...
start "Panchmukhi Backend" cmd /k "cd backend && set MAVEN_MULTIMODULEPROJECTDIRECTORY=%cd%\backend && mvnw spring-boot:run"

echo.
echo ====================================================
echo   Done! Wait 30 seconds for backend to start...
echo.
echo   Then open: http://localhost:8083/index.html
echo ====================================================
echo.
pause
