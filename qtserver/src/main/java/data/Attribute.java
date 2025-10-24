package data;

import java.io.Serializable;

/**
 * Classe astratta che rappresenta un attributo generico di un dataset.
 * <p>
 * Ogni attributo è caratterizzato da un nome e da una posizione (indice)
 * all'interno dello schema del dataset. Le sottoclassi concrete definiscono la
 * specifica tipologia di attributo, come {@link DiscreteAttribute} o
 * {@link ContinuousAttribute}.
 * </p>
 *
 * @see DiscreteAttribute
 * @see ContinuousAttribute
 */
public abstract class Attribute implements Serializable {

    /** Nome dell'attributo (non nullo e non vuoto). */
    private final String name;

    /** Indice dell'attributo nello schema del dataset (maggiore o uguale a zero). */
    private final int index;

    /**
     * Costruisce un attributo con il nome e l'indice specificati.
     *
     * @param name  il nome dell'attributo; non può essere {@code null} o vuoto
     * @param index la posizione dell'attributo nello schema; deve essere maggiore o uguale a zero
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
     * Restituisce il nome dell'attributo.
     *
     * @return il nome dell'attributo
     */
    public String getName() {
        return name;
    }

    /**
     * Restituisce la posizione dell'attributo all'interno dello schema del dataset.
     *
     * @return l'indice dell'attributo
     */
    public int getIndex() {
        return index;
    }

    /**
     * Restituisce una rappresentazione testuale dell'attributo.
     * <p>
     * In questo caso viene restituito semplicemente il nome.
     * </p>
     *
     * @return il nome dell'attributo
     */
    @Override
    public String toString() {
        return name;
    }
}