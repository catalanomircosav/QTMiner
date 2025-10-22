package exceptions;

/**
 * Eccezione controllata sollevata quando l'algoritmo di clustering genera un solo cluster.
 * 
 * @author Mirco Catalano
 * @author Lorenzo Amato
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
     */
    public ClusteringRadiusException(String message)
    {
        super(message);
    }
}