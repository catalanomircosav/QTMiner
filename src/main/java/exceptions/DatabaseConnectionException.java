package exceptions;

/**
 * Eccezione sollevata in caso di errore di connessione al database
 * 
 * @author Mirco Catalano
 * @author Lorenzo Amato
 */
public class DatabaseConnectionException extends Exception
{
    /**
     * Costruisce l'eccezione con il messaggio di default.
     */
    public DatabaseConnectionException()
    {
        super("Errore di connessione al server.");
    }

    /**
     * Costruisce l'eccezione con un messaggio personalizzato.
     * 
     * @param message messaggio dell'eccezione
     */
    public DatabaseConnectionException(String message)
    {
        super(message);
    }
}
