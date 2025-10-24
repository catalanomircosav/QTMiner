package exceptions;

/**
 * Eccezione controllata sollevata quando si verifica un errore
 * durante la connessione al database o al server responsabile
 * della gestione dei dati.
 * <p>
 * Questa eccezione viene tipicamente lanciata in fase di apertura
 * della connessione o quando il database risulta non raggiungibile.
 * </p>
 */
public class DatabaseConnectionException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Costruisce l'eccezione con un messaggio di errore predefinito.
     */
    public DatabaseConnectionException() {
        super("Errore di connessione al database.");
    }

    /**
     * Costruisce l'eccezione con un messaggio personalizzato.
     *
     * @param message il messaggio descrittivo dell'errore
     */
    public DatabaseConnectionException(String message) {
        super(message);
    }
}