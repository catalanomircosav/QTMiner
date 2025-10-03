package mining;

import data.Data;

/**
 * Collezione di {@link Cluster} gestita come array ridimensionato manualmente.
 * <p>
 * Fornisce operazioni essenziali per:
 * <ul>
 *   <li>aggiungere un cluster ({@link #add(Cluster)});</li>
 *   <li>accedere a un cluster per indice ({@link #get(int)});</li>
 *   <li>ottenere una rappresentazione testuale sintetica ({@link #toString()});</li>
 *   <li>ottenere una rappresentazione testuale dettagliata rispetto a un dataset
 *       ({@link #toString(Data)}).</li>
 * </ul>
 * L'array interno può contenere {@code null}; in tal caso, il metodo
 * {@link #toString(Data)} ignora gli elementi nulli.
 * </p>
 */
public class ClusterSet
{
    /** Array dei cluster contenuti nel set. */
    private Cluster C[] = new Cluster[0];

    /** Crea un {@code ClusterSet} vuoto. */
    public ClusterSet() { }

    /**
     * Aggiunge un cluster in coda al set.
     * <p>
     * L'implementazione rialloca un nuovo array di dimensione {@code C.length + 1}
     * e copia i riferimenti esistenti.
     * </p>
     *
     * @param c il cluster da aggiungere (può essere {@code null}, ma non è consigliato)
     */
    void add(Cluster c)
    {
        Cluster tempC[] = new Cluster[C.length + 1];
        for (int i = 0; i < C.length; i++)
            tempC[i] = C[i];
        tempC[C.length] = c;
        C = tempC;
    }

    /**
     * Restituisce il cluster all'indice specificato.
     *
     * @param i indice del cluster da recuperare (0-based)
     * @return il cluster all'indice {@code i}
     * @throws ArrayIndexOutOfBoundsException se l'indice non è compreso tra 0 e {@code C.length - 1}
     */
    Cluster get(int i) { return C[i]; }

    /**
     * Restituisce una rappresentazione sintetica del set di cluster.
     * <p>
     * Ogni riga ha la forma {@code "Cluster i: <cluster.toString()>"}.
     * </p>
     *
     * @return stringa descrittiva di tutti i cluster contenuti
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < C.length; i++)
            sb.append(i).append(": ").append(C[i].toString()).append("\n");
        
        return sb.toString();
    }

    /**
     * Restituisce una rappresentazione dettagliata del set di cluster
     * rispetto al dataset fornito.
     * <p>
     * Per ciascun cluster non nullo, delega a {@link Cluster#toString(Data)} la
     * produzione della descrizione (centroide, esempi assegnati, distanze, media).
     * </p>
     *
     * @param data il dataset di riferimento per arricchire la descrizione dei cluster
     * @return stringa dettagliata di tutti i cluster non nulli
     */
    public String toString(Data data)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < C.length; i++)
            if (C[i] != null)
                if(C.length == 1)
                    sb.append(C[i].toString(data)).append("\n");
                else
                    sb.append(i).append(": ").append(C[i].toString(data)).append("\n");
        
        return sb.toString();
    }
}
