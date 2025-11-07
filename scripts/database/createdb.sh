#!/bin/bash
# =============================================================
# Script: createdb.sh
# Descrizione: Crea il database MapDB eseguendo database.sql
# Requisiti: MySQL deve essere installato e "mysql" deve essere nel PATH
# =============================================================

MYSQL_USER="root"
MYSQL_PWD=""
SQL_FILE="database.sql"

echo "Creazione database MapDB..."
mysql -u "$MYSQL_USER" -p "$MYSQL_PWD" < "$SQL_FILE"

if [ $? -eq 0 ]; then
    echo "Database creato correttamente."
else
    echo "Errore nella creazione del database."
fi
