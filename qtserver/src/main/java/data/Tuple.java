package data;

import java.io.Serializable;
import java.util.Set;

/**
 * Rappresenta una tupla di un dataset, composta da un insieme ordinato di {@link Item}.
 * <p>
 * Fornisce metodi per accedere agli item e per calcolare:
 * <ul>
 *     <li>la distanza tra due tuple</li>
 *     <li>la distanza media rispetto a un insieme di tuple</li>
 * </ul>
 */
public class Tuple implements Serializable {

    /** Array degli item che compongono la tupla. */
    private final Item[] tuple;

    /**
     * Costruisce una tupla vuota di dimensione specificata.
     *
     * @param size numero di item della tupla (deve essere > 0)
     * @throws IllegalArgumentException se {@code size <= 0}
     */
    public Tuple(int size) {
        if (size <= 0)
            throw new IllegalArgumentException("La dimensione della tupla deve essere maggiore di zero.");
        tuple = new Item[size];
    }

    /**
     * @return numero di item nella tupla
     */
    public int getLength() {
        return tuple.length;
    }

    /**
     * Restituisce l'item all'indice indicato.
     *
     * @param i indice dell'item
     * @return l'item all'indice {@code i}
     * @throws ArrayIndexOutOfBoundsException se {@code i} non è valido
     */
    public Item get(int i) {
        if (i < 0 || i >= tuple.length)
            throw new ArrayIndexOutOfBoundsException("Indice fuori dai limiti.");
        return tuple[i];
    }

    /**
     * Inserisce un item nella tupla all'indice indicato.
     *
     * @param item item da inserire
     * @param i indice della posizione
     * @throws IndexOutOfBoundsException se {@code i} non è valido
     */
    public void add(Item item, int i) {
        if (i < 0 || i >= tuple.length)
            throw new IndexOutOfBoundsException("Indice fuori dai limiti.");
        tuple[i] = item;
    }

    /**
     * Calcola la distanza tra questa tupla e un'altra tupla di pari dimensioni.
     * <p>
     * La distanza è la somma delle distanze tra i singoli item corrispondenti.
     * </p>
     *
     * @param other tupla da confrontare
     * @return distanza totale
     *
     * @throws IllegalArgumentException se {@code other} è {@code null}
     * @throws IllegalArgumentException se le tuple hanno lunghezze diverse
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
     * Calcola la distanza media tra questa tupla e un insieme di tuple.
     *
     * @param data dataset da cui prelevare gli esempi
     * @param clusteredData indici delle tuple del dataset da confrontare
     * @return distanza media
     *
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