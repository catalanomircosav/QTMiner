@echo off
REM Script per generare Javadoc nella cartella "docs"

REM Creazione della cartella docs se non esiste
if not exist ../docs (
    mkdir ../docs
)

REM Generazione Javadoc
javadoc ../src/*.java -d ../docs/

echo Javadoc generato in cartella docs
pause
