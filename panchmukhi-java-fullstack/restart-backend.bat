@echo off
echo ===================================
echo  Restarting Backend Server
echo ===================================

cd backend

set JAVA_HOME=C:\Users\Adesh\.jdks\corretto-21.0.9-1
set PATH=%JAVA_HOME%\bin;%PATH%

echo Stopping existing backend processes...
taskkill /F /IM java.exe 2>nul
timeout /t 2 /nobreak >nul

echo.
echo Building backend...
call mvnw.cmd clean package -DskipTests

if %errorlevel% neq 0 (
    echo.
    echo ❌ Build failed! Check the errors above.
    pause
    exit /b 1
)

echo.
echo Starting backend server...
start "Backend Server" "%JAVA_HOME%\bin\java.exe" -jar target\panchmukhi-trading-1.0.0.jar

echo.
echo ✅ Backend server is starting...
echo    Navigate to http://localhost:8083/api
echo.
echo Note: Check the "Backend Server" window for logs
echo.
pause
