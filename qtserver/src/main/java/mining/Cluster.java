package mining;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import data.Data;
import data.Tuple;

/**
 * Rappresenta un singolo cluster prodotto da un algoritmo di clustering QT.
 * <p>
 * Ogni cluster è identificato da una {@link Tuple} che funge da centroide
 * e da un insieme di indici che puntano alle tuple del dataset assegnate
 * a tale cluster.
 * </p>
 *
 * Il contenuto è gestito tramite un {@link HashSet}, così da:
 * <ul>
 *   <li>evitare duplicati</li>
 *   <li>avere inserimenti/ricerche veloci</li>
 * </ul>
 *
 * @see ClusterSet
 * @see QTMiner
 */
class Cluster implements Iterable<Integer>, Comparable<Cluster>, Serializable {

    /** Centroide del cluster. */
    private final Tuple centroid;

    /** Indici delle tuple assegnate al cluster (senza duplicati). */
    private final Set<Integer> clusteredData;

    /**
     * Costruisce un cluster vuoto con il centroide specificato.
     *
     * @param centroid la tupla che rappresenta il centroide
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
     * Aggiunge un indice di tupla al cluster.
     *
     * @param id l’indice della tupla
     * @return {@code true} se la tupla è stata inserita,
     *         {@code false} se era già presente
     */
    public boolean addData(int id) {
        return clusteredData.add(id);
    }

    /**
     * Verifica se una tupla è assegnata al cluster.
     *
     * @param id l’indice della tupla
     * @return {@code true} se la tupla appartiene al cluster
     */
    public boolean contain(int id) {
        return clusteredData.contains(id);
    }

    /**
     * Rimuove una tupla dal cluster.
     *
     * @param id l’indice della tupla da rimuovere
     */
    public void removeTuple(int id) {
        clusteredData.remove(id);
    }

    /**
     * Restituisce il numero di tuple assegnate.
     *
     * @return cardinalità del cluster
     */
    public int getSize() {
        return clusteredData.size();
    }

    /**
     * Restituisce un iteratore sugli indici delle tuple contenute.
     */
    @Override
    public Iterator<Integer> iterator() {
        return clusteredData.iterator();
    }

    /**
     * Confronta questo cluster con un altro in base alla loro dimensione.
     * In caso di dimensione uguale, usa l’hashCode per evitare ambiguità
     * nel {@link java.util.TreeSet}.
     */
    @Override
    public int compareTo(Cluster other) {
        int cmp = Integer.compare(this.getSize(), other.getSize());
        return (cmp != 0) ? cmp : Integer.compare(this.hashCode(), other.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Centroid=(");

        for (int i = 0; i < centroid.getLength(); i++)
            sb.append(centroid.get(i).getValue()).append(" ");

        if (centroid.getLength() > 0)
            sb.setLength(sb.length() - 1);

        sb.append(") Size=").append(getSize());
        return sb.toString();
    }

    /**
     * Restituisce una rappresentazione dettagliata del cluster rispetto al dataset.
     *
     * @param data il dataset da cui provengono gli indici del cluster
     * @return una stringa descrittiva del cluster
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