#!/bin/bash
# Script per generare Javadoc nella cartella "docs"

# Creazione della cartella docs se non esiste
mkdir -p ../docs

# Generazione Javadoc
javadoc ../src/*.java -d ../docs

echo "Javadoc generato in cartella docs"