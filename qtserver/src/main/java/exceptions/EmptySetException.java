package exceptions;


/**
 * Eccezione controllata sollevata quando il result set è vuoto.
 * 
 * @author Lorenzo Amato
 * @author Mirco Catalano
 */
public class EmptySetException extends Exception {

    /**
     * Crea l'eccezione con un messaggio di default. 
     */
    public EmptySetException() {
        super("Il result set è vuoto.");
    }

    /** 
     * Costruisce l'eccezione con un messaggio personalizzato.
     * 
     * @param message il messaggio di errore da associare all'eccezione.
     */
    public EmptySetException(String message) {
        super(message);
    }
}