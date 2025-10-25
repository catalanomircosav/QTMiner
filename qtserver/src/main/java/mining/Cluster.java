package mining;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import data.Data;
import data.Tuple;

/**
 * Rappresenta un singolo cluster prodotto dall’algoritmo di clustering QT (Quality Threshold).
 * <p>
 * Ogni cluster è identificato da un {@link Tuple} che funge da centroide e da un insieme
 * di indici che rappresentano le tuple del dataset appartenenti al cluster.
 * I riferimenti alle tuple sono memorizzati in un {@link HashSet} per evitare duplicati
 * e garantire inserimenti e ricerche efficienti.
 * </p>
 *
 * @see ClusterSet
 * @see QTMiner
 */
public class Cluster implements Iterable<Integer>, Comparable<Cluster>, Serializable {

    /** Centroide del cluster. */
    private final Tuple centroid;

    /** Indici delle tuple assegnate al cluster (senza duplicati). */
    private final Set<Integer> clusteredData;

    /**
     * Costruisce un cluster vuoto con il centroide specificato.
     *
     * @param centroid la tupla che rappresenta il centroide del cluster
     */
    Cluster(Tuple centroid) {
        this.centroid = centroid;
        this.clusteredData = new HashSet<>();
    }

    /**
     * Restituisce il centroide del cluster.
     *
     * @return il {@link Tuple} che funge da centroide
     */
    public Tuple getCentroid() {
        return centroid;
    }

    /**
     * Aggiunge una tupla al cluster tramite il suo indice.
     *
     * @param id l’indice della tupla nel dataset
     * @return {@code true} se l’indice è stato aggiunto,
     *         {@code false} se era già presente
     */
    public boolean addData(int id) {
        return clusteredData.add(id);
    }

    /**
     * Verifica se il cluster contiene la tupla indicata.
     *
     * @param id l’indice della tupla
     * @return {@code true} se la tupla appartiene al cluster
     */
    public boolean contain(int id) {
        return clusteredData.contains(id);
    }

    /**
     * Rimuove la tupla dal cluster, se presente.
     *
     * @param id l’indice della tupla da rimuovere
     */
    public void removeTuple(int id) {
        clusteredData.remove(id);
    }

    /**
     * Restituisce il numero di tuple assegnate al cluster.
     *
     * @return la cardinalità del cluster
     */
    public int getSize() {
        return clusteredData.size();
    }

    /**
     * Restituisce un iteratore sugli indici delle tuple contenute nel cluster.
     *
     * @return l’iteratore sugli indici
     */
    @Override
    public Iterator<Integer> iterator() {
        return clusteredData.iterator();
    }

    /**
     * Confronta questo cluster con un altro cluster in base alla dimensione.
     * <p>
     * In caso di dimensione uguale, viene confrontato l’hashCode per evitare ambiguità
     * nelle strutture dati ordinate, come {@link java.util.TreeSet}.
     * </p>
     *
     * @param other il cluster da confrontare
     * @return un valore negativo, zero o positivo in base al confronto
     */
    @Override
    public int compareTo(Cluster other) {
        int cmp = Integer.compare(this.getSize(), other.getSize());
        return (cmp != 0) ? cmp : Integer.compare(this.hashCode(), other.hashCode());
    }

    /**
     * Restituisce una descrizione sintetica del cluster,
     * riportando centroide e dimensione.
     *
     * @return una stringa riassuntiva del cluster
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Centroid=(");

        for (int i = 0; i < centroid.getLength(); i++)
            sb.append(centroid.get(i)).append(", ");

        if (centroid.getLength() > 0)
            sb.setLength(sb.length() - 1);

        sb.append(")");

        return sb.toString();
    }

    /**
     * Restituisce una rappresentazione dettagliata del cluster,
     * includendo le tuple del dataset e le distanze rispetto al centroide.
     *
     * @param data il dataset da cui provengono gli indici del cluster
     * @return una stringa dettagliata con valori, distanze e distanza media
     */
    public String toString(Data data) {
        StringBuilder sb = new StringBuilder();
        sb.append("Centroid=(");

        for (int i = 0; i < centroid.getLength(); i++)
            sb.append(centroid.get(i).getValue()).append(" ");

        sb.setLength(sb.length() - 1);
        sb.append(")\nExamples:\n");

        for (Integer idx : clusteredData) {
            sb.append("[");
            for (int j = 0; j < data.getNumberOfAttributes(); j++)
                sb.append(data.getValue(idx, j)).append(" ");

            sb.setLength(sb.length() - 1);
            sb.append("] dist=")
              .append(centroid.getDistance(data.getItemSet(idx)))
              .append("\n");
        }

        sb.append("AvgDistance=")
          .append(centroid.avgDistance(data, clusteredData))
          .append("\n");

        return sb.toString();
    }
}