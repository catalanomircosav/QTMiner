package mining;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import data.Data;

/**
 * La classe {@code ClusterSet} rappresenta una collezione di {@link Cluster}
 * memorizzati in un {@link TreeSet}. Questo garantisce:
 * <ul>
 *     <li>assenza di duplicati</li>
 *     <li>ordine deterministico dei cluster tramite ordinamento naturale</li>
 * </ul>
 *
 * Viene utilizzata dall’algoritmo QT (Quality Threshold) per memorizzare
 * i cluster generati durante il processo di clustering.
 *
 * @see Cluster
 * @see QTMiner
 */
public class ClusterSet implements Iterable<Cluster>, Serializable {

    /** Insieme dei cluster memorizzati, senza duplicati e ordinato. */
    private Set<Cluster> C = new TreeSet<>();

    /**
     * Costruisce un {@code ClusterSet} vuoto.
     */
    public ClusterSet() { }

    /**
     * Aggiunge un nuovo {@link Cluster} all'insieme.
     *
     * @param c il cluster da aggiungere; non deve essere {@code null}
     * @throws NullPointerException se {@code c == null}
     */
    public void add(Cluster c) {
        if (c == null)
            throw new NullPointerException("Impossibile aggiungere un cluster null al ClusterSet.");
            
        C.add(c);
    }

    /**
     * Restituisce un iteratore sui cluster presenti nel set.
     *
     * @return un {@link Iterator} che scorre i cluster in ordine deterministico
     */
    @Override
    public Iterator<Cluster> iterator() {
        return C.iterator();
    }

    /**
     * Restituisce una rappresentazione testuale compatta del set di cluster.
     *
     * @return una stringa con i cluster numerati (uno per riga)
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 0;

        for (Cluster c : C)
            sb.append(i++).append(": ").append(c).append("\n");

        return sb.toString();
    }

    /**
     * Restituisce una rappresentazione dettagliata dei cluster
     * in relazione al dataset di riferimento.
     *
     * Ogni cluster viene espanso mostrando le tuple contenute.
     *
     * @param data il dataset usato per dettagliare il contenuto dei cluster
     * @return una stringa con descrizione dettagliata di ciascun cluster
     */
    public String toString(Data data) {
        StringBuilder sb = new StringBuilder();
        int i = 0;

        for (Cluster cluster : this)
            sb.append(i++).append(": ").append(cluster.toString(data)).append("\n");

        return sb.toString();
    }
}