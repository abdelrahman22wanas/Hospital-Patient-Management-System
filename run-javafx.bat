@echo off
echo ========================================
echo Hospital Patient Management System
echo JavaFX GUI Launcher
echo ========================================
echo.

REM Check if Maven is available
where mvn >nul 2>&1
if %errorlevel% == 0 (
    echo Using Maven to run JavaFX application...
    echo.
    mvn javafx:run
    goto :end
)

REM Fallback: Manual JavaFX execution
echo Maven not found. Attempting manual execution...
echo.
echo NOTE: You need to set JAVA_FX_PATH to your JavaFX SDK lib folder
echo Example: set JAVA_FX_PATH=C:\javafx-sdk-17.0.2\lib
echo.

if "%JAVA_FX_PATH%"=="" (
    echo ERROR: JAVA_FX_PATH environment variable not set!
    echo Please set it to your JavaFX SDK lib folder.
    echo.
    echo Example:
    echo   set JAVA_FX_PATH=C:\javafx-sdk-17.0.2\lib
    echo   run-javafx.bat
    echo.
    pause
    exit /b 1
)

REM Compile if needed
if not exist "out\HospitalFXApp.class" (
    echo Compiling JavaFX application...
    javac --module-path "%JAVA_FX_PATH%" --add-modules javafx.controls,javafx.fxml -d out -sourcepath src src\*.java
    if %errorlevel% neq 0 (
        echo Compilation failed!
        pause
        exit /b 1
    )
)

REM Run the application
echo Running JavaFX application...
echo.
java --module-path "%JAVA_FX_PATH%" --add-modules javafx.controls,javafx.fxml -cp out HospitalFXApp

:end
if %errorlevel% neq 0 (
    echo.
    echo Application exited with an error.
    pause
)

