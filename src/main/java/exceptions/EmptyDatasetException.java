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
     * 
     * Il messaggio indica che il dataset è vuoto.
     * */
    public EmptyDatasetException() 
    {
        super("Il dataset è vuoto.");
    }
        
    /** 
     * Crea l'eccezione con un messaggio personalizzato.
     * 
     * @param message il messaggio di errore da associare all'eccezione.
     * 
     * @throws IllegalArgumentException se il messaggio è {@code null} o vuoto.
    */
    public EmptyDatasetException(String message) 
    {
        super(message);
    }
}