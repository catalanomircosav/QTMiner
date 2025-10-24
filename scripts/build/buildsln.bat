@echo off
echo -------------------------------------------------
echo Build completo del progetto QTMiner...
echo -------------------------------------------------

REM Spostati nella cartella del POM padre (due livelli sopra)
cd /d %~dp0..\..

mvn clean package -DskipTests

echo -------------------------------------------------
echo Build completato!
echo Jar generati in:
echo   qtserver\target\
echo   qtclient\target\
echo -------------------------------------------------
pause
