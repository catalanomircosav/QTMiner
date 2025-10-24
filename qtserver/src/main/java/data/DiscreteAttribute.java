package data;

import java.util.TreeSet;
import java.util.Iterator;

/**
 * Rappresenta un attributo discreto di un dataset.
 * <p>
 * Un attributo discreto è caratterizzato da un insieme finito di valori
 * distinti e memorizzati in ordine lessicografico. Fornisce metodi per
 * ottenere valori e scorrere gli elementi attraverso un iteratore.
 * </p>
 *
 * @see Attribute
 */
public class DiscreteAttribute extends Attribute implements Iterable<String> {

    /** Insieme ordinato dei valori distinti dell'attributo. */
    private final TreeSet<String> values;

    /**
     * Costruisce un attributo discreto specificando nome, indice e valori
     * distinti che compongono il dominio.
     *
     * @param name   il nome dell'attributo
     * @param index  la posizione dell'attributo nello schema (maggiore o uguale a zero)
     * @param values l'insieme dei valori distinti dell'attributo
     * @throws IllegalArgumentException se {@code values} è {@code null} o vuoto
     */
    public DiscreteAttribute(String name, int index, String[] values) {
        super(name, index);

        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("L'attributo discreto deve avere almeno un valore distinto.");
        }

        this.values = new TreeSet<>();
        for (String v : values) {
            this.values.add(v);
        }
    }

    /**
     * Restituisce il numero totale di valori distinti presenti nel dominio
     * dell'attributo.
     *
     * @return il numero di valori distinti
     */
    public int getNumberOfDistinctValues() {
        return values.size();
    }

    /**
     * Restituisce il valore corrispondente alla posizione indicata
     * nell'insieme ordinato dei valori distinti.
     *
     * @param index la posizione (0-based) del valore richiesto
     * @return il valore corrispondente
     * @throws IndexOutOfBoundsException se l’indice non è compreso nei limiti validi
     */
    public String getValue(int index) {
        if (index < 0 || index >= values.size())
            throw new IndexOutOfBoundsException("Indice valore discreto fuori dai limiti.");

        return values.toArray(new String[0])[index];
    }

    /**
     * Restituisce un iteratore sui valori distinti dell'attributo.
     *
     * @return l’iteratore dell’insieme dei valori distinti
     */
    @Override
    public Iterator<String> iterator() {
        return values.iterator();
    }

    /**
     * Restituisce una rappresentazione testuale dell’attributo, composta dal
     * suo nome e dalla lista ordinata dei valori distinti.
     *
     * @return la stringa rappresentativa dell'attributo
     */
    @Override
    public String toString() {
        return getName() + " " + values;
    }
}