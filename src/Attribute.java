package src;
import java.lang.IllegalArgumentException;

/**
 * Classe astratta che rappresenta un attributo generico di un dataset. 
 * <p>
 * Ogni attributo ha un nome e un indice univoco all'interno dello schema del dataset.
 * Questa classe deve essere estesa per creare attributi specifici (ad esempio discreti o continui).
 * </p>
 * 
 * <p>
 * Gli attributi sono immutabili: il nome e l'indice non possono essere modificati dopo la creazione.
 * </p>
 * 
 * @author Mirco Catalano
 * @author Lorenzo Amato
 * 
 * @version 1.0
 * @since 1.0
 */
public abstract class Attribute {

    /** Nome dell'attributo, non nullo e non vuoto */
    private final String name;

    /** Indice dell'attributo all'interno dello schema del dataset, >= 0 */
    private final int index;

    /**
     * Costruisce un nuovo attributo con il nome e l'indice specificati.
     * 
     * @param name  il nome dell'attributo; non può essere {@code null} o vuoto
     * @param index l'indice dell'attributo; deve essere maggiore o uguale a 0
     * 
     * @throws IllegalArgumentException se {@code name} è {@code null} o vuoto
     * @throws IllegalArgumentException se {@code index} è negativo
     */
    protected Attribute(String name, int index) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Nome non valido.");
        }
        if (index < 0) {
            throw new IllegalArgumentException("Indice negativo.");
        }

        this.name = name;
        this.index = index;
    }

    /**
     * Restituisce il nome dell'attributo.
     * 
     * @return il nome dell'attributo
     */
    public String getName() {
        return name;
    }

    /**
     * Restituisce l'indice dell'attributo nello schema del dataset.
     * 
     * @return l'indice dell'attributo
     */
    public int getIndex() {
        return index;
    }

    /**
     * Restituisce una rappresentazione testuale dell'attributo.
     * <p>
     * Il valore restituito corrisponde al nome dell'attributo.
     * </p>
     * 
     * @return il nome dell'attributo
     */
    @Override
    public String toString() {
        return name;
    }
}