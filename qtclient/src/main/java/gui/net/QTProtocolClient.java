package gui.net;

import java.io.*;
import java.net.*;
import java.util.Objects;

/**
 * Client del protocollo QTMiner per GUI JavaFX.
 */
public class QTProtocolClient implements Closeable {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    public void connect(String host, int port) throws IOException {
        if (isConnected()) disconnect();
        InetAddress addr = InetAddress.getByName(Objects.requireNonNull(host));
        socket = new Socket(addr, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in  = new ObjectInputStream(socket.getInputStream());
    }

    public void disconnect() throws IOException {
        if (out != null) out.close();
        if (in  != null) in.close();
        if (socket != null) socket.close();
        out = null; in = null; socket = null;
    }

    @Override public void close() throws IOException { disconnect(); }

    // === Operazioni protocollo ===

    /** Comando 0 - Carica tabella dal database */
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

    /** Comando 1 - Clustering dal DB */
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

    /** Comando 2 - Salva cluster su file */
    public String saveClustersToFile() throws Exception {
        ensureConnected();
        out.writeObject(2);
        out.flush();

        String result = (String) in.readObject();
        if (!"OK".equals(result)) throw new Exception(result);
        return "Cluster salvati con successo.";
    }

    /** Comando 3 - Carica cluster da file */
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

    private void ensureConnected() throws IOException {
        if (!isConnected()) throw new IOException("Non connesso al server.");
    }
}