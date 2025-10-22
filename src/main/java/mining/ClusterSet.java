package mining;

import java.util.*;

import data.Data;

/**
 * Collezione di {@link Cluster} gestita un {@link TreeSet}.
 * <p>
 * Un {@code ClusterSet} rappresenta un insieme di cluster senza duplicati
 * e con un ordine di iterazione determinato dalla struttura dati interna.
 * </p>
 * 
 * @author Mirco Catalano
 * @author Lorenzo Amato
 */
public class ClusterSet implements Iterable<Cluster>
{
    /**
     * Insieme dei cluster.
     */
    private Set<Cluster> C = new TreeSet<Cluster>();

    /**
     * Crea un {@code ClusterSet} vuoto.
    */
    public ClusterSet() { }

    /**
     * Aggiunge un cluster all’insieme.
     *
     * @param c il cluster da aggiungere (può essere {@code null}, ma non è consigliato)
     */
    public void add(Cluster c)
    {
		C.add(c);
	}

    /**
     * Restituisce un iteratore sui cluster contenuti in questo set.
     *
     * @return un {@link Iterator} che scorre i {@code Cluster} dell’insieme
     */
    @Override
    public Iterator<Cluster> iterator()
    {
        return C.iterator();
    }

    /**
     * Restituisce una rappresentazione sintetica del set di cluster.
     * 
     * @return una stringa con l’elenco dei cluster in ordine di iterazione
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Cluster c : C)
            sb.append(i++).append(": ").append(c).append("\n");
        
        return sb.toString();
    }

    /**
     * Restituisce una rappresentazione dettagliata del set di cluster
     * rispetto al dataset fornito.
     *
     * @param data il dataset di riferimento usato per dettagliare i cluster
     * @return una stringa con la descrizione dettagliata di ciascun cluster
     */
    public String toString(Data data) 
    {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Cluster cluster : this) 
        {
            if (cluster != null) 
                sb.append(i).append(": ").append(cluster.toString(data)).append("\n");
            i++;
        }
        return sb.toString();
    }
}