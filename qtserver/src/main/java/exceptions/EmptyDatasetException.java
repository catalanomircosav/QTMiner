package exceptions;

/**
 * Eccezione controllata sollevata quando il dataset è vuoto.
 * 
 * @author Lorenzo Amato
 * @author Mirco Catalano
 */
public class EmptyDatasetException extends Exception
{
    /**
     * Crea l'eccezione con un messaggio di default. 
     */
    public EmptyDatasetException() 
    {
        super("Il dataset è vuoto.");
    }
        
    /** 
     * Costruisce l'eccezione con un messaggio personalizzato.
     * 
     * @param message il messaggio di errore da associare all'eccezione.
     */
    public EmptyDatasetException(String message) 
    {
        super(message);
    }
}