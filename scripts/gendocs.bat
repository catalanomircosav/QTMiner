@echo off
echo -------------------------------------------------
echo Generazione Javadoc aggregato per tutto il progetto...
echo -------------------------------------------------

REM Spostati nella cartella del progetto (quella dove c'è il POM padre)
cd /d %~dp0..
REM ora siamo in QTMiner/

mvn clean javadoc:aggregate

echo -------------------------------------------------
echo Javadoc generato!
echo Percorso: target\site\apidocs\index.html
echo -------------------------------------------------
pause
