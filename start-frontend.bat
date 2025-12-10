@echo off
chcp 65001 >nul
echo ========================================
echo   Panchmukhi Trading - Frontend Launcher
echo ========================================
echo.

cd /d "%~dp0\panchmukhi-java-fullstack\frontend"

echo Available Pages:
echo.
echo [1] Dashboard (index.html)
echo [2] News Analysis (news.html)
echo [3] Sector Intelligence (sectors.html)
echo [4] Auto Scraper Admin (scraper-admin.html)
echo [5] Open All Pages
echo [0] Exit
echo.

set /p choice="Select page to open (1-5): "

if "%choice%"=="1" (
    echo Opening Dashboard...
    start index.html
) else if "%choice%"=="2" (
    echo Opening News...
    start news.html
) else if "%choice%"=="3" (
    echo Opening Sectors...
    start sectors.html
) else if "%choice%"=="4" (
    echo Opening Auto Scraper...
    start scraper-admin.html
) else if "%choice%"=="5" (
    echo Opening All Pages...
    start index.html
    timeout /t 1 >nul
    start news.html
    timeout /t 1 >nul
    start sectors.html
    timeout /t 1 >nul
    start scraper-admin.html
) else if "%choice%"=="0" (
    exit
) else (
    echo Invalid choice!
    pause
    exit /b 1
)

echo.
echo ✅ Frontend opened in browser!
echo.
echo Make sure backend is running on http://localhost:8083
echo.
pause
