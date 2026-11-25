#!/bin/sh
# Compila sin Maven y ejecuta.  ./compilar_y_ejecutar.sh  (consola)   ./compilar_y_ejecutar.sh gui  (ventana)
set -e
cd "$(dirname "$0")"
rm -rf out && mkdir out
javac -encoding UTF-8 -d out $(find src/main/java -name "*.java")
java -Dfile.encoding=UTF-8 -cp out com.biblioteca.app.Main "$@"
