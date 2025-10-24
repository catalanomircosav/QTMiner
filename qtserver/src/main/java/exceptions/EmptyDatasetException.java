package exceptions;

/**
 * Eccezione controllata sollevata quando si tenta di eseguire
 * un'operazione su un dataset privo di esempi.
 * <p>
 * Questa eccezione viene tipicamente lanciata nelle fasi in cui
 * è necessario disporre di almeno una tupla, ad esempio durante
 * il clustering o nella lettura dei dati.
 * </p>
 */
public class EmptyDatasetException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Costruisce l'eccezione con un messaggio predefinito.
     */
    public EmptyDatasetException() {
        super("Il dataset è vuoto.");
    }

    /**
     * Costruisce l'eccezione con un messaggio personalizzato.
     *
     * @param message il messaggio descrittivo dell'errore
     */
    public EmptyDatasetException(String message) {
        super(message);
    }
}