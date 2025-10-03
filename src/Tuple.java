package src;

/**
 * La classe {@code Tuple} rappresenta una tupla di item in un dataset.
 * <p>
 * Una tupla è una collezione ordinata di item, dove ogni item è una coppia formata da un attributo e dal valore osservato per quell'attributo.
 * </p>
 * 
 * <p>
 * Fornisce metodi per accedere agli item, calcolare la distanza tra tuple e calcolare la distanza media tra la tupla corrente e un insieme di tuple.
 * </p>
 * 
 * @author Mirco Catalano
 * @author Lorenzo Amato
 * 
 * @version 1.0
 * @since 2.0
 * 
 * @see Item
 * @see Data
 */
public class Tuple {
    private Item[] tuple;

    /**
     * Costruisce una tupla vuota con la dimensione specificata.
     * 
     * @param size la dimensione della tupla (numero di item)
     * 
     * @throws IllegalArgumentException se la dimensione specificata è minore o uguale a zero
     */
    public Tuple(int size)
    {
        if(size <= 0)
            throw new IllegalArgumentException("La dimensione della tupla deve essere maggiore di zero.");

        tuple = new Item[size];
    }

    /**
     * Restituisce la lunghezza della tupla (numero di item).
     * 
     * @return la lunghezza della tupla
     */
    public int getLength() { return tuple.length; }
    
    /**
     * Restituisce l'item all'indice specificato.
     * 
     * @param i l'indice dell'item da restituire
     * @return l'item all'indice specificato
     * 
     * @throws ArrayIndexOutOfBoundsException se l'indice specificato non è valido
     */
    public Item get(int i)
    {
        if(i < 0 || i >= tuple.length)
            throw new ArrayIndexOutOfBoundsException("Indice non valido.");

        return tuple[i];
    }

    /**
     * Aggiunge l'item specificato alla tupla all'indice specificato.
     *
     * @param c l'item da aggiungere
     * 
     * @param i l'indice in cui aggiungere l'item
     */
    public void add(Item c, int i) { tuple[i] = c; }

    /**
     * Determina la distanza tra la tupla riferita da obj e la 
     * tupla corrente (riferita da this).
     * La distanza tra due tuple è definita come la somma delle distanze tra i 
     * rispettivi item.
     * 
     * @param obj la tupla con cui calcolare la distanza
     * 
     * @throws IllegalArgumentException se le tuple non sono della stessa lunghezza
     * 
     * @return la distanza tra le due tuple
     */
    public double getDistance(Tuple obj)
    {
        if(obj.getLength() != this.getLength())
            throw new IllegalArgumentException("Le tuple devono essere della stessa lunghezza.");

        double sumD = 0.0;
        for(int i = 0; i < tuple.length; i++)
            sumD += this.get(i).distance(obj.get(i).getValue());

        return sumD;
    }

    /**
     * Calcola la distanza media tra la tupla corrente e un insieme di tuple specificate dagli indici in clusteredData.
     * 
     * @param data il dataset contenente le tuple
     * @param clusteredData gli indici delle tuple con cui calcolare la distanza media
     * 
     * @throws IllegalArgumentException se clusteredData è null o vuoto
     * 
     * @return la distanza media tra la tupla corrente e le tuple specificate
     */
    public double avgDistance(Data data, int[] clusteredData)
    {  
        if(clusteredData == null || clusteredData.length == 0)
            throw new IllegalArgumentException("clusteredData non può essere null o vuoto.");

        double sumD = 0.0;
        for(int i = 0; i < clusteredData.length; i++)
            sumD += getDistance(data.getItemSet(clusteredData[i]));

        return (sumD / clusteredData.length);
    }
}
