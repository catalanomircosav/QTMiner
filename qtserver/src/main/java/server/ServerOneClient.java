package server;

import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;

import mining.QTMiner;
import data.Data;

/**
 * Gestisce una singola connessione client in un thread dedicato.
 * <p>
 * Protocollo di risposte:
 * <ul>
 *   <li>Successo: invia <b>"OK"</b> seguito da eventuali altri oggetti richiesti.</li>
 *   <li>Errore: invia una sola stringa nel formato <b>"ERROR: &lt;messaggio&gt;"</b>.</li>
 * </ul>
 * Comandi supportati (come da client):
 * <ol>
 *   <li><b>0</b> — Carica tabella dal database (in: {@code String tableName}) → out: "OK", {@code data.toString()}</li>
 *   <li><b>1</b> — Computa cluster da DB (in: {@code Double radius}) → out: "OK", {@code Integer numClusters}, {@code clusterSet.toString(data)}</li>
 *   <li><b>2</b> — Salva cluster su file → out: "OK"</li>
 *   <li><b>3</b> — Carica cluster da file (in: {@code String name}, {@code Double radius}) → out: "OK", {@code clusterSet.toString(data)}</li>
 * </ol>
 */
public class ServerOneClient extends Thread {

    private final Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private Data data;             
    private QTMiner kmeans;        
    private String lastTableName;  
    private Double lastRadius;     

    public ServerOneClient(Socket s) throws IOException {
        this.socket = s;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in  = new ObjectInputStream(socket.getInputStream());
        log("Thread creato per " + s.getInetAddress());
    }

    @Override
    public void run() {
        try {
            while (!socket.isClosed()) {
                Object cmdObj = in.readObject();
                if (!(cmdObj instanceof Integer)) {
                    sendError("Comando non valido (atteso Integer).");
                    continue;
                }
                int cmd = (Integer) cmdObj;
                handleCommand(cmd);
            }
        } catch (EOFException e) {
            log("Connessione chiusa dal client.");
        } catch (IOException e) {
            logErr("I/O: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            logErr("Classe non trovata: " + e.getMessage());
        } finally {
            closeResources();
        }
    }

    // ======================= Gestione comandi =======================

    private void handleCommand(int cmd) {
        switch (cmd) {
            case 0: handleLoadFromDb();       break;
            case 1: handleComputeFromDb();    break;
            case 2: handleSaveToFile();       break;
            case 3: handleComputeFromFile();  break;
            default:
                sendError("Comando non riconosciuto: " + cmd);
        }
    }

    private void handleLoadFromDb() {
        log("Caricamento tabella dal database...");
        try {
            Object tnameObj = in.readObject();
            if (!(tnameObj instanceof String)) {
                sendError("Nome tabella non valido.");
                return;
            }
            String tname = (String) tnameObj;
            this.lastTableName = tname;

            this.data = new Data(tname);
            sendOK(data.toString());
            log("Tabella caricata: " + tname + " -> OK");
        } catch (Exception e) {
            sendError("Impossibile caricare la tabella: " + e.getMessage());
            logErr("LoadFromDb fallito: " + e.getMessage());
        }
    }

    private void handleComputeFromDb() {
        log("Computazione cluster da DB...");
        try {
            Object rObj = in.readObject();
            if (!(rObj instanceof Double)) {
                sendError("Raggio non valido.");
                return;
            }
            double radius = (Double) rObj;
            this.lastRadius = radius;

            if (this.data == null) {
                sendError("Dataset non caricato. Esegui prima l'opzione 0.");
                return;
            }

            this.kmeans = new QTMiner(radius);
            int num = kmeans.compute(data);

            sendOK(num, kmeans.getC().toString(data));
            log("Compute DB r=" + radius + " -> OK (clusters=" + num + ")");
        } catch (Exception e) {
            sendError("Errore durante la computazione: " + e.getMessage());
            logErr("ComputeFromDb fallito: " + e.getMessage());
        }
    }

    private void handleSaveToFile() {
        log("Salvataggio cluster su file...");
        try {
            if (kmeans == null) {
                sendError("Nessun risultato da salvare. Esegui prima la computazione (opzione 1 o 3).");
                return;
            }
            if (lastTableName == null || lastRadius == null) {
                sendError("Impossibile determinare il nome del file.");
                return;
            }

            String filename = lastTableName + "_" + lastRadius + ".dmp";
            kmeans.salva(filename);

            sendOK();
            log("Salvataggio su \"" + filename + "\" -> OK");
        } catch (Exception e) {
            sendError("Errore in salvataggio: " + e.getMessage());
            logErr("SaveToFile fallito: " + e.getMessage());
        }
    }

    /**
     * Case 3 — CARICA cluster da file, NON ricalcola.
     */
    private void handleComputeFromFile() {
        log("Computazione cluster da file...");
        try {
            Object nameObj = in.readObject();
            Object rObj    = in.readObject();

            if (!(nameObj instanceof String) || !(rObj instanceof Double)) {
                sendError("Parametri non validi.");
                return;
            }

            String name = (String) nameObj;
            double radius = (Double) rObj;

            this.lastTableName = name;
            this.lastRadius = radius;

            String filename = name + "_" + radius + ".dmp";

            this.data = new Data(name);
            this.kmeans = new QTMiner(filename);

            sendOK(kmeans.getC().toString(data));
            log("Compute from file \"" + filename + "\" -> OK");
        } catch (FileNotFoundException e) {
            sendError("File non trovato: " + e.getMessage());
            logErr("File non trovato: " + e.getMessage());
        } catch (Exception e) {
            sendError("Errore nella computazione da file: " + e.getMessage());
            logErr("ComputeFromFile fallito: " + e.getMessage());
        }
    }

    // ======================= Utilità I/O =======================

    private void sendOK(Object... payload) {
        try {
            out.writeObject("OK");
            if (payload != null) {
                for (Object o : payload)
                    out.writeObject(o);
            }
            out.flush();
        } catch (IOException e) {
            logErr("Invio OK fallito: " + e.getMessage());
        }
    }

    private void sendError(String message) {
        try {
            out.writeObject("ERROR: " + (message == null ? "" : message));
            out.flush();
        } catch (IOException e) {
            logErr("Invio ERROR fallito: " + e.getMessage());
        }
    }

    private void log(String msg) {
        System.out.println("[SERVER] " + msg);
    }

    private void logErr(String msg) {
        System.err.println("[SERVER] " + msg);
    }

    private void closeResources() {
        try { if (in != null)  in.close(); }  catch (IOException ignored) {}
        try { if (out != null) out.close(); } catch (IOException ignored) {}
        try { if (!socket.isClosed()) socket.close(); } catch (IOException ignored) {}
        log("Connessione chiusa con " + socket.getInetAddress());
    }
}