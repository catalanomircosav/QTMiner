package exceptions;

/**
 * Eccezione controllata sollevata quando, durante l'esecuzione
 * dell'algoritmo di clustering QT (Quality Threshold), tutte le
 * tuple del dataset vengono inserite in un unico cluster.
 * <p>
 * Questo tipicamente accade quando il raggio di clustering scelto
 * è troppo ampio, impedendo la formazione di più cluster distinti.
 * </p>
 *
 * @see mining.QTMiner
 */
public class ClusteringRadiusException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Costruisce l'eccezione con un messaggio predefinito.
     */
    public ClusteringRadiusException() {
        super("Tutte le tuple sono state raggruppate in un unico cluster.");
    }

    /**
     * Costruisce l'eccezione con un messaggio personalizzato.
     *
     * @param message il messaggio di errore associato all'eccezione
     */
    public ClusteringRadiusException(String message) {
        super(message);
    }
}