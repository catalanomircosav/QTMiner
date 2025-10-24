package exceptions;

/**
 * Eccezione controllata sollevata quando una query o una richiesta
 * di valore non restituisce alcun risultato valido.
 * <p>
 * Questa eccezione viene solitamente lanciata in contesti in cui
 * ci si aspetta almeno un valore da elaborare, ma l’operazione
 * restituisce un insieme vuoto o un valore assente.
 * </p>
 */
public class NoValueException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Costruisce l'eccezione con un messaggio di default.
     */
    public NoValueException() {
        super("Non ci sono valori nel risultato.");
    }

    /**
     * Costruisce l'eccezione specificando un messaggio personalizzato.
     *
     * @param message il messaggio descrittivo dell’errore
     */
    public NoValueException(String message) {
        super(message);
    }
}