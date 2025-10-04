package mining;

import data.Data;
import java.util.*;

/**
 * Collezione di {@link Cluster} gestita un {@link TreeSet}.
 * <p>
 * Un {@code ClusterSet} rappresenta un insieme di cluster senza duplicati
 * e con un ordine di iterazione determinato dalla struttura dati interna.
 * <p>
 * Fornisce operazioni essenziali per:
 * <ul>
 *   <li>{@link #add(Cluster)} – inserisce un cluster nell’insieme;</li>
 *   <li>{@link #iterator()} – restituisce un iteratore sui cluster;</li>
 *   <li>{@link #toString()} – rappresentazione sintetica dell’insieme;</li>
 *   <li>{@link #toString(Data)} – rappresentazione dettagliata rispetto a un {@link Data}.</li>
 * </ul>
 * <p>
 * L’ordine di iterazione dei cluster è definito dal {@code TreeSet} e non
 * rappresenta posizioni indicizzate. L’inserimento di un cluster duplicato
 * (non univoco secondo la relazione d’ordine del {@code TreeSet}) non ha effetto.
 * </p>
 */
public class ClusterSet implements Iterable<Cluster>
{
    /**
     * Insieme dei cluster. L’ordine di iterazione corrisponde a quello
     * definito dal {@link TreeSet} e non rappresenta posizioni indicizzate.
     */
    private Set<Cluster> C = new TreeSet<>();

    /** Crea un {@code ClusterSet} vuoto. */
    public ClusterSet() { }

    /**
     * Aggiunge un cluster all’insieme.
     * <p>
     * Se il cluster è considerato duplicato in base alla relazione d’ordine del
     * {@code TreeSet}, l’inserimento non ha effetto. L’ordine di iterazione
     * può variare in funzione del criterio d’ordinamento adottato.
     * </p>
     *
     * @param c il cluster da aggiungere (può essere {@code null}, ma non è consigliato)
     */
    public void add(Cluster c) {
		C.add(c);
	}

    /**
     * Restituisce un iteratore sui cluster contenuti in questo set.
     * <p>
     * L’iteratore rispetta l’ordine del {@code TreeSet}. Modifiche strutturali
     * al set dopo la creazione dell’iteratore possono causare comportamenti
     * non definiti durante l’iterazione.
     * </p>
     *
     * @return un {@link Iterator} che scorre i {@code Cluster} dell’insieme
     */
    @Override
    public Iterator<Cluster> iterator() {
        return C.iterator();
    }

    /**
     * Restituisce una rappresentazione sintetica del set di cluster.
     * <p>
     * Ogni riga riporta un indice crescente (derivato dall’ordine di iterazione)
     * e la rappresentazione testuale del cluster: {@code "<indice>: <cluster>"}.
     * L’indice indicato è puramente informativo e non rappresenta la posizione
     * in una struttura indicizzata.
     * </p>
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
     * <p>
     * Per ciascun cluster (in ordine di iterazione del {@code TreeSet}) viene
     * riportato un indice crescente e la descrizione dettagliata prodotta da
     * {@link Cluster#toString(Data)} – tipicamente centroide, esempi assegnati,
     * distanza dal centroide e distanza media.
     * </p>
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
