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
 * Gestisce una singola connessione con un client tramite socket TCP.
 * <p>
 * Ogni istanza di questa classe viene eseguita in un thread separato,
 * permettendo al server di servire più client contemporaneamente.
 * </p>
 *
 * <h2>Protocollo di risposta</h2>
 * <ul>
 *     <li><b>OK</b> → seguito dagli eventuali oggetti aggiuntivi richiesti</li>
 *     <li><b>ERROR: messaggio</b> → in caso di errore applicativo o operativo</li>
 * </ul>
 *
 * <h2>Comandi supportati</h2> 
 * <ol>
 *     <li><b>0</b> → Carica tabella dal DB <i>(in: String tableName)</i></li>
 *     <li><b>1</b> → Computa cluster dal DB <i>(in: Double radius)</i></li>
 *     <li><b>2</b> → Salva su file il cluster generato</li>
 *     <li><b>3</b> → Carica cluster da file <i>(in: String name, Double radius)</i></li>
 * </ol>
 */
public class ServerOneClient extends Thread {

    private final Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    /** Dataset attualmente caricato (comando 0 o 3). */
    private Data data;

    /** Oggetto QTMiner utilizzato per il clustering. */
    private QTMiner kmeans;

    /** Ultimo nome tabella usato (necessario per il salvataggio su file). */
    private String lastTableName;

    /** Ultimo raggio usato (necessario per il salvataggio su file). */
    private Double lastRadius;

    /**
     * Costruisce un nuovo gestore per la connessione con un singolo client.
     *
     * @param socket la socket associata al client già accettato da {@link MultiServer}
     * @throws IOException se fallisce la creazione degli stream di input/output
     */
    public ServerOneClient(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in  = new ObjectInputStream(socket.getInputStream());
        log("Thread creato per " + socket.getInetAddress());
    }

    /**
     * Ciclo principale del thread: ascolta i comandi del client e li inoltra ai metodi dedicati.
     */
    @Override
    public void run() {
        try {
            while (!socket.isClosed()) {
                Object cmdObj = in.readObject();
                if (!(cmdObj instanceof Integer)) {
                    sendError("Comando non valido (atteso Integer).");
                    continue;
                }
                handleCommand((Integer) cmdObj);
            }
        } catch (EOFException e) {
            log("Connessione chiusa dal client.");
        } catch (IOException | ClassNotFoundException e) {
            logErr("Errore I/O: " + e.getMessage());
        } finally {
            closeResources();
        }
    }

    /**
     * Dispatch del comando ricevuto dal client.
     *
     * @param cmd il codice del comando inviato dal client
     */
    private void handleCommand(int cmd) {
        switch (cmd) {
            case 0: handleLoadFromDb();      break;
            case 1: handleComputeFromDb();   break;
            case 2: handleSaveToFile();      break;
            case 3: handleComputeFromFile(); break;
            default:
                sendError("Comando non riconosciuto: " + cmd);
        }
    }

    /**
     * Comando 0 — Carica una tabella dal database e invia al client la sua rappresentazione testuale.
     */
    private void handleLoadFromDb() {
        log("Caricamento tabella dal database...");
        try {
            Object tnameObj = in.readObject();
            if (!(tnameObj instanceof String)) {
                sendError("Nome tabella non valido.");
                return;
            }

            lastTableName = (String) tnameObj;
            data = new Data(lastTableName);

            sendOK(data.toString());
            log("Tabella caricata: " + lastTableName);
        } catch (Exception e) {
            sendError("Impossibile caricare la tabella: " + e.getMessage());
        }
    }

    /**
     * Comando 1 — Esegue clustering QT direttamente dal database.
     */
    private void handleComputeFromDb() {
        log("Computazione cluster da DB...");
        try {
            Object rObj = in.readObject();
            if (!(rObj instanceof Double)) {
                sendError("Raggio non valido.");
                return;
            }

            lastRadius = (Double) rObj;
            if (data == null) {
                sendError("Dataset non caricato. Esegui prima l'opzione 0.");
                return;
            }

            kmeans = new QTMiner(lastRadius);
            int num = kmeans.compute(data);

            sendOK(num, kmeans.getC().toString(data));
        } catch (Exception e) {
            sendError("Errore durante la computazione: " + e.getMessage());
        }
    }

    /**
     * Comando 2 — Salva il cluster corrente su file.
     */
    private void handleSaveToFile() {
        log("Salvataggio cluster su file...");
        try {
            if (kmeans == null) {
                sendError("Nessun cluster da salvare.");
                return;
            }
            if (lastTableName == null || lastRadius == null) {
                sendError("Impossibile determinare il nome del file.");
                return;
            }

            String filename = lastTableName + "_" + lastRadius + ".dmp";
            kmeans.salva(filename);
            sendOK();
        } catch (Exception e) {
            sendError("Errore durante il salvataggio: " + e.getMessage());
        }
    }

    /**
     * Comando 3 — Carica cluster da file e lo restituisce al client (senza ricalcolo).
     */
    private void handleComputeFromFile() {
        log("Caricamento cluster da file...");
        try {
            Object nameObj = in.readObject();
            Object rObj = in.readObject();
            if (!(nameObj instanceof String) || !(rObj instanceof Double)) {
                sendError("Parametri non validi.");
                return;
            }

            lastTableName = (String) nameObj;
            lastRadius = (Double) rObj;
            String filename = lastTableName + "_" + lastRadius + ".dmp";

            data = new Data(lastTableName);
            kmeans = new QTMiner(filename);

            sendOK(kmeans.getC().toString(data));
        } catch (FileNotFoundException e) {
            sendError("File non trovato.");
        } catch (Exception e) {
            sendError("Errore durante il caricamento: " + e.getMessage());
        }
    }

    /**
     * Invia al client un messaggio di successo con eventuali payload aggiuntivi.
     *
     * @param payload gli oggetti da inviare dopo il messaggio "OK"
     */
    private void sendOK(Object... payload) {
        try {
            out.writeObject("OK");
            for (Object o : payload) {
                out.writeObject(o);
            }
            out.flush();
        } catch (IOException e) {
            logErr("Invio OK fallito: " + e.getMessage());
        }
    }

    /**
     * Invia al client un messaggio di errore formattato.
     *
     * @param message il messaggio di errore
     */
    private void sendError(String message) {
        try {
            out.writeObject("ERROR: " + message);
            out.flush();
        } catch (IOException e) {
            logErr("Invio ERROR fallito: " + e.getMessage());
        }
    }

    /**
     * Stampa un log informativo lato server.
     *
     * @param msg il messaggio da mostrare su stdout
     */
    private void log(String msg) {
        System.out.println("[SERVER] " + msg);
    }

    /**
     * Stampa un log di errore lato server.
     *
     * @param msg il messaggio da mostrare su stderr
     */
    private void logErr(String msg) {
        System.err.println("[SERVER] " + msg);
    }

    /**
     * Chiude socket e stream associati a questa connessione client.
     */
    private void closeResources() {
        try { if (in != null)  in.close(); }  catch (IOException ignored) {}
        try { if (out != null) out.close(); } catch (IOException ignored) {}
        try { if (!socket.isClosed()) socket.close(); } catch (IOException ignored) {}
        log("Connessione chiusa con " + socket.getInetAddress());
    }
}