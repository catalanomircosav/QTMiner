#!/bin/bash

echo "-------------------------------------------------"
echo "Generazione Javadoc aggregato per tutto il progetto..."
echo "-------------------------------------------------"

# Spostati nella cartella del progetto (dove si trova il POM padre)
cd "$(dirname "$0")/.." || exit 1
# ora siamo in QTMiner/

mvn clean javadoc:aggregate

echo "-------------------------------------------------"
echo "Javadoc generato!"
echo "Percorso: target/site/apidocs/index.html"
echo "-------------------------------------------------"

read -p "Premi invio per continuare..."