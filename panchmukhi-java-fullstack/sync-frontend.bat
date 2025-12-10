@echo off
echo Syncing Frontend Files to Backend Static Resources...
echo.

:: Create static directory if it doesn't exist
if not exist "backend\src\main\resources\static" (
    mkdir "backend\src\main\resources\static"
)

:: Copy all frontend files to backend static directory
robocopy "frontend" "backend\src\main\resources\static" /E /NFL /NDL /NJH /NJS /R:2 /W:1

if %errorlevel% LEQ 3 (
    echo.
    echo [SUCCESS] Frontend files synced successfully!
    echo You can now access your app at:
    echo   - http://localhost:8083/index.html  (Backend serves static + APIs)
    echo   - http://localhost:3000/index.html  (Frontend dev server + APIs from 8083)
) else (
    echo.
    echo [ERROR] Failed to sync files. Error code: %errorlevel%
)

echo.
pause
