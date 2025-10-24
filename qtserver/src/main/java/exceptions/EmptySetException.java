package exceptions;

/**
 * Eccezione controllata sollevata quando si tenta di accedere o utilizzare
 * un result set vuoto, ad esempio dopo una query che non ha prodotto risultati.
 * <p>
 * Può verificarsi sia in fase di interazione con il database, sia nella logica
 * applicativa che si aspetta un insieme non vuoto di elementi.
 * </p>
 */
public class EmptySetException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Costruisce l'eccezione con un messaggio predefinito.
     */
    public EmptySetException() {
        super("Il result set è vuoto.");
    }

    /**
     * Costruisce l'eccezione con un messaggio personalizzato.
     *
     * @param message il messaggio descrittivo dell'errore
     */
    public EmptySetException(String message) {
        super(message);
    }
}
