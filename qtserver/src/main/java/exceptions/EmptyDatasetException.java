package exceptions;

/**
 * Eccezione controllata sollevata quando si tenta di eseguire
 * un'operazione su un dataset privo di esempi.
 * <p>
 * Questa eccezione viene tipicamente sollevata nelle situazioni
 * in cui è necessario disporre di almeno una tupla, ad esempio
 * durante il clustering o nella fase di lettura dei dati.
 * </p>
 */
public class EmptyDatasetException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Costruisce l'eccezione con un messaggio di default.
     */
    public EmptyDatasetException() {
        super("Il dataset è vuoto.");
    }

    /**
     * Costruisce l'eccezione specificando un messaggio personalizzato.
     *
     * @param message il messaggio descrittivo dell'errore
     */
    public EmptyDatasetException(String message) {
        super(message);
    }
}