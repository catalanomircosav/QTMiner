package exceptions;

/**
 * Eccezione controllata sollevata nel caso in cui la query non restituisca alcun risultato.
 * 
 * @see Exception
 * 
 * @author Mirco Catalano
 * @author Lorenzo Amato
 */
public class NoValueException extends Exception {
    public NoValueException()
    {
        super("Non ci sono valori nel risultato.");
    }

    public NoValueException(String message)
    {
        super(message);
    }
}