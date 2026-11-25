@echo off
REM Compila sin Maven y ejecuta.  compilar_y_ejecutar.bat  (consola)   compilar_y_ejecutar.bat gui  (ventana)
chcp 65001 >nul
cd /d "%~dp0"
if exist out rmdir /s /q out
mkdir out
dir /s /b src\main\java\*.java > fuentes.txt
javac -encoding UTF-8 -d out @fuentes.txt
if errorlevel 1 exit /b 1
java -Dfile.encoding=UTF-8 -cp out com.biblioteca.app.Main %*
