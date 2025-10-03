package src;

/**
 * Rappresenta un cluster in un algoritmo di clustering.
 * <p>
 * Ogni cluster è definito da un {@link Tuple} che funge da centroide
 * e da un insieme di indici di tuple/righe del dataset che risultano
 * assegnate al cluster (gestito tramite {@code ArraySet}).
 * </p>
 * 
 * @author Lorenzo Amato
 * @author Mirco Catalano
 * 
 * @version 1.0
 * @since 2.0
 */
class Cluster
{

    /** Centroide del cluster. */
    private Tuple centroid;

    /**
     * Insieme degli identificativi (tipicamente indici di riga) delle
     * tuple assegnate a questo cluster.
     */
    private ArraySet clusteredData;

    /**
     * Crea un cluster inizializzandolo con il centroide specificato e
     * un insieme vuoto di elementi assegnati.
     *
     * @param centroid la tupla che rappresenta il centroide del cluster
     */
    Cluster(Tuple centroid)
    {
        this.centroid = centroid;
        clusteredData = new ArraySet();
    }

    /**
     * Restituisce il centroide del cluster.
     *
     * @return il {@link Tuple} che funge da centroide
     */
    Tuple getCentroid() { return centroid; }

    /**
     * Aggiunge l'identificativo di una tupla al cluster.
     *
     * @param id l'identificativo (indice) della tupla da assegnare
     * @return {@code true} se la tupla entra nel cluster (ovvero non era già presente),
     *         {@code false} se era già assegnata a questo cluster
     */
    boolean addData(int id) { return clusteredData.add(id); }

    /**
     * Verifica se una tupla (identificata da {@code id}) è attualmente
     * assegnata a questo cluster.
     *
     * @param id l'identificativo (indice) della tupla
     * @return {@code true} se la tupla è contenuta nell'insieme del cluster,
     *         {@code false} altrimenti
     */
    boolean contain(int id) { return clusteredData.get(id); }

    /**
     * Rimuove la tupla (identificata da {@code id}) dal cluster,
     * ad esempio quando cambia assegnazione verso un altro cluster.
     *
     * @param id l'identificativo (indice) della tupla da rimuovere
     */
    void removeTuple(int id) { clusteredData.delete(id); }
    
    public int getSize() { return clusteredData.size(); }
	
	
	public int[] iterator() { return clusteredData.toArray(); }

    /**
     * Restituisce una rappresentazione testuale del cluster
     * rispetto al dataset fornito.
     * 
     * @param data il dataset di riferimento
     * @return stringa descrittiva del cluster
     */
    public String toString(Data data)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Centroid = (");

		for(int i = 0; i < centroid.getLength(); i++)
			sb.append(centroid.get(i).getValue()).append(" ");
        
        sb.setLength(sb.length() - 1); // rimuove l'ultimo spazio
        sb.append(")\nExamples:\n");

		int[] array = clusteredData.toArray();

		for(int i = 0; i < array.length; i++)
        {
            sb.append("[");
			for(int j=0;j<data.getNumberOfAttributes();j++)
                sb.append(data.getValue(array[i], j)).append(" ");

            sb.setLength(sb.length() - 1); // rimuove l'ultimo spazio
            sb.append("] dist = ").append(getCentroid().getDistance(data.getItemSet(array[i]))).append("\n");			
		}
        sb.append("\nAvgDistance=").append(getCentroid().avgDistance(data, array)).append("\n");
        sb.setLength(sb.length() - 1); // rimuove l'ultimo a capo
        
		return sb.toString();
	}
}
