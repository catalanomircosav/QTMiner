package test.java;

/**
 * Classe che rappresenta un'eccezione sollevata dal server durante
 * l'elaborazione di una richiesta del client.
 * <p>
 * Questa eccezione viene generata quando il server restituisce un
 * messaggio di errore al client. Il messaggio associato all'eccezione
 * contiene la descrizione dell'errore comunicata dal server.
 * </p>
 * 
 * <p>
 * La classe viene utilizzata all'interno di {@link MainTest} per gestire
 * gli errori trasmessi dal server durante le operazioni di connessione,
 * caricamento dei dati o salvataggio dei cluster.
 * </p>
 * 
 * @author Mirco Catalano
 * @author Lorenzo Amato
 */
public class ServerException extends Exception
{
    /**
     * Identificatore di versione della classe per la serializzazione
     */
    private static final long serialVersionUID = 1L;

    /**
     * Costruisce un'eccezione {@code ServerException} senza messaggio.
     */
    public ServerException()
    {
        super();
    }

    /**
     * Costruisce un'eccezione {@code ServerException} con il messaggio specificato.
     * 
     * @param message messaggio descrittivo dell'errore generato dal server
     */
    public ServerException(String message)
    {
        super(message);
    }

    /**
     * Costruisce un'eccezione {@code ServerException} con messaggio e causa specificati.
     * 
     * @param message messaggio descrittivo dell'errore generato dal server
     * @param cause causa dell'eccezione
     */
    public ServerException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Costruisce un'eccezione {@code ServerException} con la causa specificata.
     * 
     * @param cause causa dell'eccezione
     */
    public ServerException(Throwable cause)
    {
        super(cause);
    }
}
