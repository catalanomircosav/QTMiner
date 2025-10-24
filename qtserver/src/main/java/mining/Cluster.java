package mining;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import data.Data;
import data.Tuple;
/**
 * Rappresenta un cluster in un algoritmo di clustering.
 * <p>
 * Ogni cluster è definito da una Tupla che funge da centroide
 * e da un insieme di indici di tuple/righe del dataset che risultano
 * assegnate al cluster (gestito tramite {@code ArraySet}).
 * </p>
 * 
 * @author Lorenzo Amato
 * @author Mirco Catalano
 */
class Cluster implements Iterable<Integer>, Comparable<Cluster>, Serializable
{
    /**
     * Centroide del cluster
    */
    private Tuple centroid;

    /**
     * Insieme degli degli id tuple assegnate a questo cluster.
     */
    private HashSet<Integer> clusteredData;

    /**
     * Costruisce un cluster inizializzandolo con il centroide specificato e
     * un insieme vuoto di elementi assegnati.
     *
     * @param centroid la tupla che rappresenta il centroide del cluster
     */
    Cluster(Tuple centroid)
    {
        this.centroid = centroid;
        clusteredData = new HashSet<Integer>();
    }

    /**
     * Restituisce il centroide del cluster.
     *
     * @return il {@link Tuple} che funge da centroide
     */
    public Tuple getCentroid()
    {
        return centroid;
    }

    /**
     * Aggiunge l'identificativo di una tupla al cluster.
     *
     * @param id l'identificativo (indice) della tupla da assegnare
     * 
     * @return {@code true} se la tupla entra nel cluster,
     *         {@code false} se era già assegnata a questo cluster
     */
    public boolean addData(int id)
    {
        return clusteredData.add(id);
    }

    /**
     * Verifica se una tupla (identificata da {@code id}) è attualmente
     * assegnata a questo cluster.
     *
     * @param id l'identificativo (indice) della tupla
     * 
     * @return {@code true} se la tupla è contenuta nell'insieme del cluster,
     *         {@code false} altrimenti
     */
    public boolean contain(int id)
    {
        return clusteredData.contains(id);
    }

    /**
     * Rimuove la tupla (identificata da {@code id}) dal cluster,
     * ad esempio quando cambia assegnazione verso un altro cluster.
     *
     * @param id l'identificativo (indice) della tupla da rimuovere
     */
    public void removeTuple(int id)
    {
        clusteredData.remove(id);
    }

    /**
     * Restituisce la dimensione del cluster
     * 
     * @return il numero di tuple appartenenti al cluster
     */
    public int getSize()
    {
        return clusteredData.size();
    }

    /**
     * Restituisce un iteratore sugli identificativi delle tuple
     * assegnate a questo cluster.
     * 
     * @return un iteratore sugli indici delle tuple nel cluster
     */
    @Override
    public java.util.Iterator<Integer> iterator()
    {
        return clusteredData.iterator();
    }

    /**
     * Confronta questo cluster con un altro in base alla loro dimensione.
     * 
     * @param other il cluster con cui effettuare il confronto
     * 
     * @return un valore negativo se questo cluster contiene meno tuple di {@code other},
     *         zero se ne contengono lo stesso numero,
     *         un valore positivo se ne contiene di più
     */
    @Override
    public int compareTo(Cluster other)
    {
        if (this.getSize() == other.getSize())
            return Integer.compare(this.hashCode(), other.hashCode());
        
        return this.getSize() < other.getSize() ? -1 : 1;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("Centroid=(");
        for (int i = 0; i < centroid.getLength(); i++)
            sb.append(centroid.get(i).getValue()).append(" ");

        if (centroid.getLength() > 0) {
            sb.setLength(sb.length() - 1);
        }
        sb.append(")");
        sb.append(" Size=").append(getSize());

        return sb.toString();
    }

    /**
     * Restituisce una rappresentazione testuale del cluster
     * rispetto al dataset fornito.
     * 
     * @param data il dataset di riferimento
     * 
     * @return stringa descrittiva del cluster
     */
    public String toString(Data data)
    {
        StringBuilder sb = new StringBuilder();
        Set<Integer> array = clusteredData;
        
        sb.append("Centroid = (");
        for (int i = 0; i < centroid.getLength(); i++)
            sb.append(centroid.get(i).getValue()).append(" ");

        sb.setLength(sb.length() - 1);
        sb.append(")\nExamples:\n");
        for (Integer idx : array)
        {
            sb.append("[");
            for (int j = 0; j < data.getNumberOfAttributes(); j++)
                sb.append(data.getValue(idx, j)).append(" ");

            sb.setLength(sb.length() - 1);

            sb.append("] dist = ")
            .append(getCentroid().getDistance(data.getItemSet(idx)))
            .append("\n");
        }

        sb.append("AvgDistance=")
        .append(getCentroid().avgDistance(data, array)).append("\n");

        return sb.toString();
    }
}