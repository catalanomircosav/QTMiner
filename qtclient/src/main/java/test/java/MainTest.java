package test.java;

import java.net.Socket;
import java.net.InetAddress;
import java.net.SocketException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import keyboardinput.Keyboard;

/**
 * Client di test per la connessione verso un server remoto.
 * Gestisce tramite socket diverse operazioni richieste dal menu, come:
 * <ul>
 *   <li>Caricare una tabella dal DB</li>
 *   <li>Computare i cluster dai dati caricati dal DB</li>
 *   <li>Salvare i cluster su file</li>
 *   <li>Computare i cluster da file</li>
 * </ul>
 * 
 * Il protocollo di comunicazione è basato su {@link ObjectInputStream} e {@link ObjectOutputStream}.
 * 
 * 
 * 
 */
public class MainTest {

    /** Stream di output verso il server. */
    private ObjectOutputStream out;

    /** Stream di input dal server. */
    private ObjectInputStream in;

    /**
     * Costruisce un client e apre una connessione verso il server specificato.
     *
     * @param ip    indirizzo IP del server
     * @param port  porta su cui il server è in ascolto
     * 
     * @throws IOException se fallisce la connessione o la creazione degli stream
     */
    public MainTest(String ip, int port) throws IOException {
        InetAddress addr = InetAddress.getByName(ip);
        System.out.println("addr = " + addr);

        Socket socket = new Socket(addr, port);
        System.out.println(socket);

        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Mostra il menu e invia la scelta al server.
     *
     * @return intero corrispondente alla scelta dell’utente (0–3)
     */
    private int menu() {
        int answer = 1;

        try {
            do {
                System.out.println("\n---MENU ---");
                System.out.println("(0) Carica tabella dal database");
                System.out.println("(1) Computa cluster dal database");
                System.out.println("(2) Salva cluster su file");
                System.out.println("(3) Computa cluster da file");
                System.out.print("Scelta (0/1/2/3): ");

                answer = Keyboard.readInt();
            } while (answer < 0 || answer > 3);

            out.writeObject(answer);
            out.flush();
        } catch (IOException exception) {
            System.err.println("Errore menu: " + exception.getMessage());
        }

        return answer;
    }

    /**
     * Opzione (0): Carica una tabella dal DB tramite nome tabella.
     *
     * @throws SocketException        se si verifica un errore di rete
     * @throws ServerException        se il server restituisce un errore
     * @throws IOException            se fallisce la comunicazione
     * @throws ClassNotFoundException se la risposta non è deserializzabile
     */
    private void storeTableFromDb()
            throws SocketException, ServerException, IOException, ClassNotFoundException {
        System.out.print("Nome tabella: ");
        String tabName = Keyboard.readString();
        out.writeObject(tabName);
        out.flush();

        String result = (String) in.readObject();
        if (!result.equals("OK"))
            throw new ServerException(result);

        System.out.println((String) in.readObject());
    }

    /**
     * Opzione (1): Computa i cluster leggendo i dati da DB.
     *
     * @return stringa contenente il ClusterSet
     * 
     * @throws SocketException          se si verifica un errore di rete
     * @throws ServerException          se il server restituisce un errore
     * @throws IOException              se fallisce la comunicazione
     * @throws ClassNotFoundException   se la risposta non è deserializzabile
     */
    private String learningFromDbTable()
            throws SocketException, ServerException, IOException, ClassNotFoundException {
        double r;
        do
        {
            System.out.print("Radius: ");
            r = Keyboard.readDouble();
        } while (r <= 0);

        out.writeObject(r);
        out.flush();

        String result = (String) in.readObject();
        if (result.equals("OK"))
        {
            System.out.println("Number of Clusters: " + in.readObject());
            return (String) in.readObject();
        }
        else throw new ServerException(result);
    }

    /**
     * Opzione (2): Salva i cluster in un file.
     *
     * @throws SocketException          se si verifica un errore di rete
     * @throws ServerException          se il server restituisce un errore
     * @throws IOException              se fallisce la comunicazione
     * @throws ClassNotFoundException   se la risposta non è deserializzabile
     */
    private void storeClusterInFile()
            throws SocketException, ServerException, IOException, ClassNotFoundException {
        String result = (String) in.readObject();
        if (!result.equals("OK"))
            throw new ServerException(result);
    }

    /**
     * Opzione (3): Computa cluster leggendo i dati da file.
     *
     * @return stringa contenente il ClusterSet
     * 
     * @throws SocketException          se si verifica un errore di rete
     * @throws ServerException          se il server restituisce un errore
     * @throws IOException              se fallisce la comunicazione
     * @throws ClassNotFoundException   se la risposta non è deserializzabile
     */
    private String learningfromFile()
            throws SocketException, ServerException, IOException, ClassNotFoundException {
        System.out.print("Nome tabella: ");
        out.writeObject(Keyboard.readString());

        System.out.print("Raggio: ");
        out.writeObject(Keyboard.readDouble());
        out.flush();

        String result = (String) in.readObject();
        if (result.equals("OK"))
            return (String) in.readObject();
        else throw new ServerException(result);
    }

    /**
     * Avvia il programma client.
     *
     * @param args <ip> <porta>
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Uso: <ip> <porta>");
            return;
        }

        String ip = args[0];
        int port = Integer.parseInt(args[1]);
        MainTest main = null;

        try {
            main = new MainTest(ip, port);
        }
        catch (IOException exception) {
            System.err.println(exception.getMessage());
            return;
        }

        do {
            int menuAnswer = main.menu();
            try {
                switch (menuAnswer) {
                    case 0:
                        main.storeTableFromDb();
                        break;

                    case 1:
                        System.out.println("Cluster dal database:");
                        System.out.println(main.learningFromDbTable());
                        break;

                    case 2:
                        main.storeClusterInFile();
                        System.out.println("Cluster salvati in file con successo");
                        break;

                    case 3:
                        System.out.println("Cluster dal file:");
                        System.out.println(main.learningfromFile());
                        break;

                    default:
                        System.out.println("Opzione non valida!");
                        break;
                }
            } 
            catch (Exception e) {
                System.err.println("Errore: " + e.getMessage());
            }

            System.out.print("Vuoi eseguire un'altra operazione? (s/n): ");
            if (Keyboard.readChar() != 's') break;
        } while (true);

        try {
            main.out.close();
            main.in.close();
        }
        catch (IOException e) {
            System.err.println("Errore chiusura connessione: " + e.getMessage());
        }
    }
}