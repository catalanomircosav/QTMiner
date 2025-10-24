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
 * La classe {@code QTMiner} implementa l'algoritmo di clustering QT
 * (Quality Threshold), che raggruppa i dati in cluster in base ad un
 * raggio massimo assegnato.
 *
 * L'algoritmo costruisce, per ogni punto, un cluster candidato e ne
 * seleziona ogni volta il più popoloso finché tutti i dati risultano
 * assegnati.
 *
 * @see Data
 * @see Tuple
 * @see Cluster
 * @see ClusterSet
 *
 * 
 * 
 */
public class QTMiner {

    /** Insieme dei cluster prodotti dall'algoritmo. */
    private ClusterSet C;

    /** Raggio massimo entro cui i punti possono appartenere allo stesso cluster. */
    private double radius;

    /**
     * Costruisce un nuovo {@code QTMiner} specificando il raggio di clustering.
     *
     * @param radius raggio massimo; deve essere positivo
     *
     * @throws IllegalArgumentException se {@code radius <= 0}
     */
    public QTMiner(double radius) {
        if (radius <= 0)
            throw new IllegalArgumentException("Il raggio deve essere maggiore di zero.");

        this.C = new ClusterSet();
        this.radius = radius;
    }

    /**
     * Costruisce un {@code QTMiner} leggendo un {@link ClusterSet} da file.
     *
     * @param filename percorso del file da cui leggere l'oggetto serializzato
     *
     * @throws FileNotFoundException    se il file non esiste
     * @throws IOException              se avviene un errore durante la lettura
     * @throws ClassNotFoundException   se l'oggetto nel file non corrisponde alla classe attesa
     */
    public QTMiner(String filename)
            throws FileNotFoundException, IOException, ClassNotFoundException {

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(filename))) {

            this.C = (ClusterSet) in.readObject();
        }
    }

    /**
     * Serializza e salva su file il {@link ClusterSet} corrente.
     *
     * @param filename percorso del file di destinazione
     *
     * @throws FileNotFoundException    se il file non può essere creato
     * @throws IOException              se avviene un errore in scrittura
     */
    public void salva(String filename) throws FileNotFoundException, IOException {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(C);
        }
    }

    /**
     * Restituisce il set di cluster generato.
     *
     * @return il {@link ClusterSet} prodotto
     */
    public ClusterSet getC() {
        return C;
    }

    /**
     * Esegue l'algoritmo QT sul dataset fornito.
     *
     * @param data il dataset su cui eseguire il clustering
     *
     * @return il numero di cluster generati
     *
     * @throws ClusteringRadiusException    se l'algoritmo produce un solo cluster
     */
    public int compute(Data data) throws ClusteringRadiusException {
        int numclusters = 0;
        boolean[] isClustered = new boolean[data.getNumberOfExamples()];
        Arrays.fill(isClustered, false);

        int countClustered = 0;
        while (countClustered != data.getNumberOfExamples()) {
            Cluster c = buildCandidateCluster(data, isClustered);
            C.add(c);

            numclusters++;
            for (Integer idx : c)
                isClustered[idx] = true;

            countClustered += c.getSize();
        }

        if (numclusters == 1)
            throw new ClusteringRadiusException();

        return numclusters;
    }

    /**
     * Costruisce e restituisce il cluster candidato più popoloso, scegliendo
     * come centro una tupla non ancora assegnata.
     *
     * @param data        il dataset di riferimento
     * @param isClustered array booleano che indica quali tuple sono già assegnate
     *
     * @return il cluster candidato con la maggiore cardinalità
     */
    public Cluster buildCandidateCluster(Data data, boolean[] isClustered) {
        Cluster best = null;
        int maxSize = -1;

        for (int i = 0; i < data.getNumberOfExamples(); i++) {
            if (!isClustered[i]) {
                Tuple centroid = data.getItemSet(i);
                Cluster candidate = new Cluster(centroid);

                for (int j = 0; j < data.getNumberOfExamples(); j++) {
                    if (!isClustered[j]) {
                        Tuple tuple = data.getItemSet(j);
                        double distance = centroid.getDistance(tuple);
                        if (distance <= radius)
                            candidate.addData(j);
                    }
                }

                if (candidate.getSize() > maxSize) {
                    best = candidate;
                    maxSize = candidate.getSize();
                }
            }
        }

        return best;
    }
}