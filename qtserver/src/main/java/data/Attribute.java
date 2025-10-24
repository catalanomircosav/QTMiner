package data;

import java.io.Serializable;

/**
 * Classe astratta che rappresenta un attributo generico di un dataset.
 * <p>
 * Ogni attributo ha un nome e una posizione (indice) nello schema del dataset.
 * Le sottoclassi dovranno definire il tipo di attributo (es. discreto o continuo).
 * </p>
 *
 * @see DiscreteAttribute
 * @see ContinuousAttribute
 */
public abstract class Attribute implements Serializable {

    /** Nome dell'attributo (non nullo e non vuoto). */
    private final String name;

    /** Indice dell'attributo nello schema del dataset (>= 0). */
    private final int index;

    /**
     * Costruisce un attributo con nome e indice specificati.
     *
     * @param name  nome dell'attributo; non può essere {@code null} o vuoto
     * @param index posizione dell'attributo; deve essere >= 0
     *
     * @throws IllegalArgumentException se {@code name} è {@code null} o vuoto
     * @throws IllegalArgumentException se {@code index} è negativo
     */
    protected Attribute(String name, int index) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Nome attributo non valido.");
        if (index < 0)
            throw new IllegalArgumentException("Indice attributo negativo.");

        this.name = name;
        this.index = index;
    }

    /**
     * @return nome dell'attributo
     */
    public String getName() {
        return name;
    }

    /**
     * @return indice dell'attributo nello schema
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return rappresentazione testuale dell'attributo (il suo nome)
     */
    @Override
    public String toString() {
        return name;
    }
}