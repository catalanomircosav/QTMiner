package data;

import java.util.TreeSet;
import java.util.Iterator;

/**
 * Rappresenta un attributo discreto di un dataset.
 * <p>
 * Un attributo discreto è definito da un insieme finito di valori distinti,
 * memorizzati in ordine lessicografico.
 * </p>
 *
 * @see Attribute
 */
public class DiscreteAttribute extends Attribute implements Iterable<String> {

    /** Insieme ordinato dei valori distinti dell'attributo. */
    private final TreeSet<String> values;

    /**
     * Costruisce un attributo discreto specificando nome, indice e valori possibili.
     *
     * @param name   nome dell'attributo
     * @param index  posizione dell'attributo nello schema (>= 0)
     * @param values insieme dei valori distinti dell'attributo
     *
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
     * @return numero di valori distinti dell'attributo
     */
    public int getNumberOfDistinctValues() {
        return values.size();
    }

    /**
     * Restituisce il valore in base alla posizione nell'insieme ordinato.
     *
     * @param index posizione (0-based)
     * @return valore corrispondente
     * @throws IndexOutOfBoundsException se l'indice non è valido
     */
    public String getValue(int index) {
        if (index < 0 || index >= values.size())
            throw new IndexOutOfBoundsException("Indice valore discreto fuori dai limiti.");

        return values.toArray(new String[0])[index];
    }

    /**
     * @return iteratore dell'insieme di valori distinti
     */
    @Override
    public Iterator<String> iterator() {
        return values.iterator();
    }

    /**
     * @return rappresentazione testuale dell'attributo
     */
    @Override
    public String toString() {
        return getName() + " " + values;
    }
}