package gui.net;

import java.io.*;
import java.net.*;
import java.util.Objects;

/**
 * Implementazione lato client del protocollo di comunicazione per il progetto
 * <b>QTMiner</b> (Quality Threshold Clustering), destinata all'interfaccia GUI in JavaFX.
 * <p>
 * Questa classe gestisce la connessione con il server QTMiner e consente di
 * eseguire le principali operazioni di clustering, caricamento e salvataggio dati
 * tramite un protocollo numerico (comandi 0–3).
 * <p>
 * Ogni operazione invia un codice di comando al server e riceve in risposta
 * oggetti serializzati contenenti messaggi o risultati del clustering.
 * 
 * <h2>Comandi del protocollo</h2>
 * <ul>
 *   <li><b>0</b> – Caricamento tabella dal database</li>
 *   <li><b>1</b> – Esecuzione clustering da database</li>
 *   <li><b>2</b> – Salvataggio dei cluster su file</li>
 *   <li><b>3</b> – Caricamento cluster da file</li>
 * </ul>
 * 
 * @author Lorenzo Amato
 * @author Mirco Catalano
 */
public class QTProtocolClient implements Closeable {

    
    /** Socket di connessione al server QTMiner. */
    private Socket socket;

    /** Stream di output per l'invio dei comandi e dei dati al server. */
    private ObjectOutputStream out;

    /** Stream di input per la ricezione delle risposte dal server. */
    private ObjectInputStream in;

    /**
     * Costruttore predefinito del client del protocollo QTMiner.
     * <p>
     * Crea un'istanza non connessa; la connessione va stabilita tramite {@link #connect(String, int)}.
     */
    public QTProtocolClient() {
        // nessuna inizializzazione specifica
    }
    
    /**
     * Verifica se il client è attualmente connesso al server.
     *
     * @return {@code true} se il socket è attivo e connesso, {@code false} altrimenti
     */
    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    /**
     * Stabilisce una connessione TCP/IP con il server QTMiner specificato.
     * Se una connessione è già attiva, viene chiusa prima di aprirne una nuova.
     *
     * @param host indirizzo del server (hostname o IP)
     * @param port porta su cui il server è in ascolto
     * @throws IOException se la connessione non può essere stabilita
     * @throws NullPointerException se l'host è {@code null}
     */
    public void connect(String host, int port) throws IOException {
        if (isConnected()) disconnect();
        InetAddress addr = InetAddress.getByName(Objects.requireNonNull(host));
        socket = new Socket(addr, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in  = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Chiude la connessione con il server e rilascia tutte le risorse associate.
     *
     * @throws IOException se si verifica un errore durante la chiusura dei flussi
     */
    public void disconnect() throws IOException {
        if (out != null) out.close();
        if (in  != null) in.close();
        if (socket != null) socket.close();
        out = null;
        in = null;
        socket = null;
    }

    /**
     * Implementazione dell'interfaccia {@link Closeable} per consentire
     * l'utilizzo di questa classe all'interno di un blocco try-with-resources.
     *
     * @throws IOException se si verifica un errore durante la disconnessione
     */
    @Override
    public void close() throws IOException { disconnect(); }

    // === Operazioni protocollo ===

    /**
     * Comando <b>0</b>: richiede al server di caricare una tabella dal database.
     *
     * @param tableName nome della tabella da caricare
     * @return rappresentazione testuale della tabella caricata
     * @throws Exception se il server restituisce un errore o la connessione fallisce
     */
    public String loadTableFromDb(String tableName) throws Exception {
        ensureConnected();
        out.writeObject(0);
        out.flush();
        out.writeObject(tableName);
        out.flush();

        String result = (String) in.readObject();
        if (!"OK".equals(result)) throw new Exception(result);
        return (String) in.readObject();
    }

    /**
     * Comando <b>1</b>: esegue l'algoritmo di clustering Quality Threshold (QT)
     * sui dati caricati dal database.
     *
     * @param radius raggio di soglia da utilizzare per il clustering
     * @return stringa contenente il numero di cluster trovati e i relativi dettagli
     * @throws Exception se il server restituisce un errore o la connessione fallisce
     */
    public String clusterFromDb(double radius) throws Exception {
        ensureConnected();
        out.writeObject(1);
        out.flush();
        out.writeObject(radius);
        out.flush();

        String result = (String) in.readObject();
        if (!"OK".equals(result)) throw new Exception(result);
        Object num = in.readObject();
        Object clusters = in.readObject();
        return "Cluster trovati: " + num + "\n" + clusters;
    }

    /**
     * Comando <b>2</b>: richiede al server di salvare su file i cluster
     * generati dall'ultima operazione di clustering.
     *
     * @return messaggio di conferma del salvataggio
     * @throws Exception se il server restituisce un errore o la connessione fallisce
     */
    public String saveClustersToFile() throws Exception {
        ensureConnected();
        out.writeObject(2);
        out.flush();

        String result = (String) in.readObject();
        if (!"OK".equals(result)) throw new Exception(result);
        return "Cluster salvati con successo.";
    }

    /**
     * Comando <b>3</b>: richiede al server di caricare un set di cluster
     * precedentemente salvato su file.
     *
     * @param table nome della tabella di riferimento
     * @param radius raggio di soglia del clustering da ricostruire
     * @return rappresentazione testuale dei cluster caricati
     * @throws Exception se il server restituisce un errore o la connessione fallisce
     */
    public String clusterFromFile(String table, double radius) throws Exception {
        ensureConnected();
        out.writeObject(3);
        out.flush();
        out.writeObject(table);
        out.writeObject(radius);
        out.flush();

        String result = (String) in.readObject();
        if (!"OK".equals(result)) throw new Exception(result);
        return (String) in.readObject();
    }

    /**
     * Garantisce che il client sia connesso al server prima dell'esecuzione
     * di un'operazione di protocollo.
     *
     * @throws IOException se il client non è connesso
     */
    private void ensureConnected() throws IOException {
        if (!isConnected()) throw new IOException("Non connesso al server.");
    }
}