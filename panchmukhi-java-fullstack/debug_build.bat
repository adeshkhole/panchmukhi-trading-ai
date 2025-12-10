@echo off
cd backend
set JAVA_HOME=C:\Users\Adesh\.jdks\corretto-21.0.9-1
set PATH=%JAVA_HOME%\bin;%PATH%
echo JAVA_HOME is %JAVA_HOME%
echo Running build...
call mvnw.cmd clean package -DskipTests > build_debug.log 2>&1
echo Build complete. Check build_debug.log
