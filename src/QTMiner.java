package src;

import java.util.Arrays;
/**
 * La classe {@code QTMiner} implementa un algoritmo di clustering basato su un approccio di tipo "quasi-topologico".
 * <p>
 * L'algoritmo suddivide un dataset in cluster basandosi su una distanza massima (raggio) specificata.
 * Ogni cluster è rappresentato da un centroide e da un insieme di tuple assegnate al cluster.
 * </p>
 * 
 * @author Mirco Catalano
 * @author Lorenzo Amato
 * 
 * @version 1.0
 * @since 2.0
 * 
 * @see Data
 * @see Tuple
 * @see Cluster
 * @see ClusterSet
 */
public class QTMiner {

    private ClusterSet C;
    private double radius;

    /**
     * Costruisce un'istanza di {@code QTMiner} con il raggio specificato.
     * 
     * @param radius il raggio massimo per la formazione dei cluster; deve essere maggiore di zero
     * 
     * @throws IllegalArgumentException se il raggio specificato è minore o uguale a zero
     */
    public QTMiner(double radius)
    {
        if(radius <= 0)
            throw new IllegalArgumentException("Il raggio deve essere maggiore di zero.");

        C = new ClusterSet(); 
        this.radius = radius;
    }

    /**
     * Restituisce il set di cluster generato dall'algoritmo.
     * @return il set di cluster generato
     */
    public ClusterSet getC() { return C; }

    /**
     * Esegue l'algoritmo di clustering sul dataset fornito.
     * 
     * @param data il dataset su cui eseguire il clustering
     * @return il numero di cluster generati
     */
    public int compute(Data data) 
    {
        int numclusters = 0;   

        boolean[] isClustered = new boolean[data.getNumberOfExamples()]; 
        Arrays.fill(isClustered, false);

        int countClustered = 0;
        while(countClustered != data.getNumberOfExamples())
        {
            Cluster c = buildCandidateCluster(data, isClustered);
            C.add(c);

            numclusters++;

            int clusteredTupleId[] = c.iterator();
            for(int i = 0; i < clusteredTupleId.length; i++)
                isClustered[clusteredTupleId[i]] = true;
                
            countClustered += c.getSize();
        }

        return numclusters;
    }

    /**
     * Costruisce un cluster per ciascuna tupla di data non 
     * ancora clusterizzata in un cluster di C e restituisce il cluster
     * candidato più popoloso 
     * 
     * @param data il dataset di riferimento
     * @param isClustered array booleano che indica se una tupla è già stata clusterizzata
     * @return il cluster candidato più popoloso
     */
    public Cluster buildCandidateCluster(Data data, boolean[] isClustered)
    {
        Cluster best = null;
        int maxSize = -1;

        for(int i = 0; i < data.getNumberOfExamples(); i++)
        {
            if(!isClustered[i])
            {
                Tuple centroid = data.getItemSet(i);
                Cluster candidate = new Cluster(centroid);

                for(int j = 0; j < data.getNumberOfExamples(); j++)
                {
                    if(!isClustered[j])
                    {
                        Tuple tuple = data.getItemSet(j);
                        double distance = centroid.getDistance(tuple);
                        if(distance <= radius)
                            candidate.addData(j);
                    }
                }

                if(candidate.getSize() > maxSize)
                {
                    best = candidate;
                    maxSize = candidate.getSize();
                }
            }
        }
        return best;
    }
}
