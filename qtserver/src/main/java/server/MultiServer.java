package server;

import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

/**
 * Server multi-thread che accetta più connessioni client
 * e per ciascuna avvia un thread dedicato {@link ServerOneClient}.
 * <p>
 * Il server rimane in ascolto sulla porta specificata e,
 * per ogni nuova connessione, delega la comunicazione
 * ad un thread separato.
 * </p>
 */
public class MultiServer {

    /** Porta su cui il server rimane in ascolto. */
    private final int port;

    /**
     * Costruisce un {@code MultiServer} sulla porta specificata.
     *
     * @param port numero di porta su cui mettersi in ascolto
     */
    public MultiServer(int port) {
        this.port = port;
    }

    /**
     * Avvia il server e rimane in ascolto di nuove connessioni.
     */
    public void start() {

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server in ascolto sulla porta: " + port);

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Nuova connessione da: " + clientSocket.getInetAddress());

                    new ServerOneClient(clientSocket).start();
                }
                catch (IOException e) {
                    System.err.println("Errore nella gestione del client: " + e.getMessage());
                }
            }
        }
        catch (IOException e) {
            System.err.println("Errore nell'apertura della porta " + port + ": " + e.getMessage());
        }
    }

    /**
     * Avvia il server passando la porta come argomento da linea di comando.
     * 
     * @params args <port>
     */
    public static void main(String[] args) {

        int port = 8080;

        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            }
            catch (NumberFormatException e) {
                System.err.println("Porta non valida, uso quella di default: " + port);
            }
        }

        new MultiServer(port).start();
    }
}