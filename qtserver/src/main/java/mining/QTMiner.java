package mining;

import data.Data;
import data.Tuple;

import java.util.Arrays;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;

import exceptions.ClusteringRadiusException;

/**
 * La classe {@code QTMiner} implementa un algoritmo di clustering.
 * 
 * @see Data
 * @see Tuple
 * @see Cluster
 * @see ClusterSet
 * 
 * @author Mirco Catalano
 * @author Lorenzo Amato
 */
public class QTMiner {

    /**
     * L'insieme di Cluster
     */
    private ClusterSet C;

    /**
     * Raggio dei cluster
     */
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
     * Costruisce un ClusterSet leggendolo da un file
     * 
     * @param filename percorso del file da cui leggere il file
     * 
     * @throws FileNotFoundException se il file non viene trovato
     * @throws IOException se ci sono errori di input/output
     * @throws ClassNotFoundException se la classe non e' stata trovata
     */
    public QTMiner(String filename) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        try
        {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));

            C = ((ClusterSet) in.readObject());
        }
        catch(FileNotFoundException exception)
        {
            System.err.println(exception.getMessage());
        }
        catch(IOException exception)
        {
            System.err.println(exception.getMessage());
        }
        catch(ClassNotFoundException exception)
        {
            System.err.println(exception.getMessage());
        }
    }

    /**
     * Salva il {@code ClusterSet} in un file con percorso specificato
     * 
     * @param filename percorso su cui salvare il file
     * 
     * @throws FileNotFoundException se il file non e' trovato
     * @throws IOException se ci sono problemi di input/output
     */
    public void salva(String filename) throws FileNotFoundException, IOException
    {
        try
        {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));

            out.writeObject(C);
        }
        catch(FileNotFoundException exception)
        {
            System.err.println(exception.getMessage());
        }
        catch(IOException exception)
        {
            System.err.println(exception.getMessage());
        }
    }

    /**
     * Restituisce il set di cluster generato dall'algoritmo.
     * 
     * @return il set di cluster generato
     */
    public ClusterSet getC() { return C; }

    /**
     * Esegue l'algoritmo di clustering sul dataset fornito.
     * 
     * @param data il dataset su cui eseguire il clustering
     * 
     * @throws ClusteringRadiusException se l'algoritmo genera un solo cluster
     * 
     * @return il numero di cluster generati
     */
    public int compute(Data data) throws ClusteringRadiusException
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
            for(Integer idx : c)
                isClustered[idx] = true;
                
            countClustered += c.getSize();
        }

        if(numclusters == 1)
            throw new ClusteringRadiusException();
            
        return numclusters;
    }

    /**
     * Costruisce un cluster per ciascuna tupla di data non 
     * ancora clusterizzata in un cluster di C e restituisce 
     * il cluster candidato più popoloso.
     * 
     * @param data il dataset di riferimento
     * @param isClustered array booleano che indica se una tupla è già stata clusterizzata
     * 
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
                        if(distance <= radius) candidate.addData(j);
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