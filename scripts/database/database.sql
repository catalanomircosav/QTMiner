-- =========================================================
-- Script di inizializzazione MapDB (MySQL 8+)
-- Derivato dalle specifiche dell'esercitazione su Serializzazione & JDBC
-- =========================================================

-- 1) Creazione (pulita) del database
DROP DATABASE IF EXISTS MapDB;
CREATE DATABASE MapDB;

-- 2) Creazione utente e assegnazione privilegi su MapDB
CREATE USER IF NOT EXISTS 'MapUser'@'localhost' IDENTIFIED BY 'map';
GRANT CREATE, SELECT, INSERT, DELETE ON MapDB.* TO 'MapUser'@'localhost';
FLUSH PRIVILEGES;

-- 3) Selezione database
USE MapDB;

-- 4) Creazione tabella playtennis
--    Schema conforme al PDF: outlook, temperature, umidity, wind, play
DROP TABLE IF EXISTS playtennis;
CREATE TABLE playtennis (
    outlook     VARCHAR(10),
    temperature FLOAT(5,2),
    umidity     VARCHAR(10),
    wind        VARCHAR(10),
    play        VARCHAR(10)
);

-- 5) Popolamento tabella playtennis (14 tuple)
INSERT INTO playtennis VALUES ('sunny',    30.3,  'high',   'weak',   'no');
INSERT INTO playtennis VALUES ('sunny',    30.3,  'high',   'strong', 'no');
INSERT INTO playtennis VALUES ('overcast', 30.0,  'high',   'weak',   'yes');
INSERT INTO playtennis VALUES ('rain',     13.0,  'high',   'weak',   'yes');
INSERT INTO playtennis VALUES ('rain',      0.0,  'normal', 'weak',   'yes');
INSERT INTO playtennis VALUES ('rain',      0.0,  'normal', 'strong', 'no');
INSERT INTO playtennis VALUES ('overcast',  0.1,  'normal', 'strong', 'yes');
INSERT INTO playtennis VALUES ('sunny',    13.0,  'high',   'weak',   'no');
INSERT INTO playtennis VALUES ('sunny',     0.1,  'normal', 'weak',   'yes');
INSERT INTO playtennis VALUES ('rain',     12.0,  'normal', 'weak',   'yes');
INSERT INTO playtennis VALUES ('sunny',    12.5,  'normal', 'strong', 'yes');
INSERT INTO playtennis VALUES ('overcast', 12.5,  'high',   'strong', 'yes');
INSERT INTO playtennis VALUES ('overcast', 29.21, 'normal', 'weak',   'yes');
INSERT INTO playtennis VALUES ('rain',     12.5,  'high',   'strong', 'no');

-- Fine script
