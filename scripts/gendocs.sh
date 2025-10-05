#!/bin/bash
# Script per generare Javadoc nella cartella "docs"

# Creazione della cartella docs se non esiste
mkdir -p ../docs

# Generazione Javadoc per tutti i file .java nelle sottocartelle
javadoc -d ../docs -sourcepath ../src ../src/MainTest.java ../src/data/*.java ../src/exceptions/*.java ../src/keyboardinput/*.java ../src/mining/*.java

echo "Javadoc generato in cartella docs"