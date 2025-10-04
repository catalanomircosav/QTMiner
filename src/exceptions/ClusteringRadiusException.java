package exceptions;

/**
 * Eccezione controllata sollevata quando l'algoritmo di clustering genera un solo cluster.
 * 
 * @author Mirco Catalano
 * @author Lorenzo Amato
 * 
 * @version 1.0
 * @since 4.0
 */
public class ClusteringRadiusException extends Exception
{
    /**
     * Crea l'eccezione con un messaggio di default.
     * Il messaggio indica che tutte le tuple sono state raggruppate in un unico cluster.
     */
    public ClusteringRadiusException()
    {
        super("Tutte le tuple sono state raggruppate in un unico cluster.");
    }

    /**
     * Crea l'eccezione con un messaggio personalizzato.
     * @param message il messaggio di errore da associare all'eccezione.
     * 
     * @throws IllegalArgumentException se il messaggio è {@code null} o vuoto.
     */
    public ClusteringRadiusException(String message)
    {
        if(message == null || message.isEmpty())
            throw new IllegalArgumentException("Messaggio non valido.");

        super(message);
    }
}
