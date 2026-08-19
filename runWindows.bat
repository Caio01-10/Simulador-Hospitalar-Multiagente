@echo off
:: Arquivo de comandos para compilação e execução do projeto
:: Windows - USE o prompt de comando não o power shell

if not exist bin mkdir bin

echo Compilando...
javac -cp "src;%USERPROFILE%\.m2\repository\org\processing\core\4.5.0\core-4.5.0.jar" -d bin src\*.java

echo Executando...
java -cp "bin;%USERPROFILE%\.m2\repository\org\processing\core\4.5.0\core-4.5.0.jar" -ea Sketch
pause