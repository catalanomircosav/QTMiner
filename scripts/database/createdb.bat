@echo off
REM ============================================================
REM Script: createdb.bat
REM Descrizione: Crea il database MapDB eseguendo createdb.sql
REM Requisiti: MySQL deve essere installato e "mysql" deve essere nel PATH
REM ============================================================

set MYSQL_USER=root
set MYSQL_PWD=
set SQL_FILE=database.sql

echo Creazione database MapDB...
mysql -u %MYSQL_USER% -p%MYSQL_PWD% < %SQL_FILE%

if %ERRORLEVEL%==0 (
    echo Database creato correttamente.
) else (
    echo Errore nella creazione del database.
)
pause