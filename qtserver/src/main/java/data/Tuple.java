package data;

import java.io.Serializable;
import java.util.Set;

/**
 * Rappresenta una tupla di un dataset, composta da un insieme ordinato di {@link Item}.
 * <p>
 * Fornisce metodi per accedere agli item e per calcolare:
 * </p>
 * <ul>
 *     <li>la distanza totale rispetto a un'altra tupla</li>
 *     <li>la distanza media rispetto a un insieme di tuple</li>
 * </ul>
 */
public class Tuple implements Serializable {

    /** Array degli item che compongono la tupla. */
    private final Item[] tuple;

    /**
     * Costruisce una tupla vuota della dimensione specificata.
     *
     * @param size il numero di item della tupla (deve essere maggiore di zero)
     * @throws IllegalArgumentException se {@code size <= 0}
     */
    public Tuple(int size) {
        if (size <= 0)
            throw new IllegalArgumentException("La dimensione della tupla deve essere maggiore di zero.");
        tuple = new Item[size];
    }

    /**
     * Restituisce il numero di item presenti nella tupla.
     *
     * @return il numero di item
     */
    public int getLength() {
        return tuple.length;
    }

    /**
     * Restituisce l'item situato nella posizione indicata.
     *
     * @param i l’indice dell’item da restituire
     * @return l'item corrispondente
     * @throws ArrayIndexOutOfBoundsException se l’indice non è compreso nei limiti validi
     */
    public Item get(int i) {
        if (i < 0 || i >= tuple.length)
            throw new ArrayIndexOutOfBoundsException("Indice fuori dai limiti.");
        return tuple[i];
    }

    /**
     * Inserisce un item nella posizione indicata della tupla.
     *
     * @param item l’item da inserire
     * @param i    l’indice della posizione
     * @throws IndexOutOfBoundsException se l’indice non è valido
     */
    public void add(Item item, int i) {
        if (i < 0 || i >= tuple.length)
            throw new IndexOutOfBoundsException("Indice fuori dai limiti.");
        tuple[i] = item;
    }

    /**
     * Calcola la distanza tra questa tupla e un'altra tupla della stessa dimensione.
     * <p>
     * La distanza complessiva è ottenuta sommando le distanze tra gli item corrispondenti.
     * </p>
     *
     * @param other la tupla con cui confrontare questa tupla
     * @return la distanza totale tra le due tuple
     * @throws IllegalArgumentException se {@code other} è {@code null} o ha dimensione diversa
     */
    public double getDistance(Tuple other) {
        if (other == null)
            throw new IllegalArgumentException("La tupla da confrontare non può essere null.");
        if (other.getLength() != this.getLength())
            throw new IllegalArgumentException("Le tuple devono avere la stessa lunghezza.");

        double sumD = 0.0;
        for (int i = 0; i < tuple.length; i++)
            sumD += this.get(i).distance(other.get(i).getValue());

        return sumD;
    }

    /**
     * Calcola la distanza media tra questa tupla e un insieme di tuple
     * appartenenti a un dataset.
     *
     * @param data          il dataset contenente le tuple
     * @param clusteredData l’insieme degli indici delle tuple da confrontare
     * @return la distanza media rispetto all’insieme specificato
     * @throws IllegalArgumentException se {@code clusteredData} è {@code null} o vuoto
     */
    public double avgDistance(Data data, Set<Integer> clusteredData) {
        if (clusteredData == null || clusteredData.isEmpty())
            throw new IllegalArgumentException("Il set di indici non può essere null o vuoto.");

        double sumD = 0.0;
        for (Integer idx : clusteredData)
            sumD += getDistance(data.getItemSet(idx));

        return sumD / clusteredData.size();
    }
}