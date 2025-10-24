#!/bin/bash

echo "-------------------------------------------------"
echo "Build completo del progetto QTMiner..."
echo "-------------------------------------------------"

# Spostati nella cartella del progetto (dove si trova il POM padre)
cd "$(dirname "$0")/../.." || exit 1
# ora siamo in QTMiner/

mvn clean package -DskipTests

echo "-------------------------------------------------"
echo "Build completato!"
echo "Jar generati in:"
echo "  qtserver/target/"
echo "  qtclient/target/"
echo "-------------------------------------------------"

read -p "Premi invio per continuare..."