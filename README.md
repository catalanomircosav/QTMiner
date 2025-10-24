# QTMiner - QT Clustering Project

QTMiner è un progetto **Client/Server** per l'analisi dei dati tramite l'algoritmo di clustering **Quality Threshold (QT)**.

Il sistema permette di:

- Caricare un dataset da un database MySQL
- Calcolare i cluster con un raggio scelto dall’utente
- Salvare i cluster su file
- Ricaricarli e visualizzarli in un secondo momento

---

## 🏗 Architettura del progetto

Il sistema è suddiviso in due moduli **Maven**:
```
QTMiner/
│ pom.xml (progetto padre)
│
├─ qtclient/ (client CLI)
└─ qtserver/ (server multi-thread)
```

### Modello di comunicazione:
```
CLIENT (CLI)
|
| TCP Socket
|
SERVER ----> MySQL DB
|
---> QTMiner (algoritmo QT)
```

---

## ✅ Funzionalità principali

| Operazione | Lato | Descrizione |
|------------|-------|-------------|
| Caricamento dataset | Client → Server → DB | Lettura tabella da MySQL |
| Clustering QT | Server | Generazione cluster su dataset caricato |
| Salvataggio su file | Server | Serializzazione in `.dmp` |
| Ricarica da file | Client → Server | Clustering o visualizzazione senza DB |
| Logging & error handling | Entrambi | Risposte `OK` / `ERROR: <msg>` |

Il **client** offre un menu testuale e invia comandi al server (`0-3`).  
Il **server** gestisce più client tramite thread, esegue i comandi e accede a DB e algoritmo QT.

---

## 🔌 Protocollo di comunicazione

Il client invia un codice numerico al server. Ogni comando produce una risposta `OK` + dati, oppure `ERROR: messaggio`.

| Codice | Significato | Input richiesto | Output |
|---------|------------|----------------|--------|
| `0` | Carica tabella dal DB | `String tableName` | `OK`, dataset |
| `1` | Clustering da DB | `Double radius` | `OK`, numero cluster, dettaglio cluster |
| `2` | Salva cluster su file | — | `OK` |
| `3` | Clustering da file | `String name`, `Double radius` | `OK`, cluster-set |

Formato errore: ```ERROR: messaggio```

---

## 🧠 Algoritmo Quality-Threshold (QT)

L’algoritmo forma cluster scegliendo iterativamente il cluster **più popoloso entro un raggio R**, basandosi sulla distanza tra tuple.

Ogni cluster contiene **tutte le tuple entro distanza ≤ R** dal proprio centroide.

Supporta:

- **Attributi discreti** → distanza 0/1
- **Attributi continui** → distanza normalizzata `[0,1]`

---

## ⚙️ Requisiti

| Componente | Versione consigliata |
|------------|---------------------|
| Java | 17+ (build target 25) |
| Maven | 3.8+ |
| MySQL | 8.x |

---

## 🚀 Esecuzione

### 1. Avvia il server
```bash
cd qtserver/
mvn clean package
java -jar target/qtserver-1.0.jar <port>
```
2. Avvia il client
```bash
cd qtclient/
mvn clean package
java -jar target/qtclient-1.0.jar <ip> <port>
```
## Estensioni previste
| Estensione | Stato
|-|-|
| GUI JavaFX per il client|	In sviluppo |
|Script .sql per generare il DB automaticamente	| Pianificato |
|Script .sh/.bat per build ed esecuzione rapida |	Pianificato | 
| Workflow CI con GitHub Actions | Pianificato |

___

## Autori
Sviluppo realizzato in collaborazione paritaria:
- Mirco Catalano
- Lorenzo Amato