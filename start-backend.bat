@echo off
chcp 65001 >nul
echo ========================================
echo   Panchmukhi Trading - Backend Startup
echo ========================================
echo.

cd /d "%~dp0"

echo [1/4] Checking Java...
java -version 2>nul
if errorlevel 1 (
    echo ❌ Java not found! Install Java 17 or higher
    pause
    exit /b 1
)
echo ✅ Java OK

echo.
echo [2/4] Checking Maven...
if exist "panchmukhi-java-fullstack\mvnw.cmd" (
    echo ✅ Maven wrapper found
) else (
    echo ❌ Maven wrapper not found!
    echo Creating Maven wrapper...
    cd panchmukhi-java-fullstack
    call mvn wrapper:wrapper
    cd ..
)

echo.
echo [3/4] Checking Database Connection...
echo Make sure PostgreSQL is running!
echo Database: panchmukhi_trading
echo Port: 5432
echo.
pause

echo.
echo [4/4] Starting Backend Server...
echo URL: http://localhost:8083
echo.
echo Press Ctrl+C to stop
echo.

cd panchmukhi-java-fullstack

REM Try Maven wrapper first
if exist "mvnw.cmd" (
    echo Using Maven Wrapper...
    call mvnw.cmd -pl backend spring-boot:run
) else (
    REM Fall back to system Maven
    echo Using System Maven...
    cd backend
    call mvn spring-boot:run
)

pause
