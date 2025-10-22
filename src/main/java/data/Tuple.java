package data;

import java.util.Set;

/**
 * Classe che rappresenta una tupla in un dataset.
 * <p>
 * Una tupla e' una collezione ordinata di item.
 * 
 * La classe fornisce metodi per accedere agli item, calcolarne la distanza e la distanza media.
 * </p>
 * 
 * @see Item
 * @see Data
 * 
 * @author Mirco Catalano
 * @author Lorenzo Amato
 */
public class Tuple
{
    /**
     * Array di item
     */
    private Item[] tuple;

    /**
     * Costruisce una tupla vuota con la dimensione specificata.
     * 
     * @param size dimensione da assegnare alla tupla
     * 
     * @throws IllegalArgumentException se la dimensione specificata e' minore o uguale a 0
     */
    public Tuple(int size)
    {
        if(size <= 0)
            throw new IllegalArgumentException("Dimensione non valida.");
        
        tuple = new Item[size];
    }

    /**
     * Restituisce la dimensione della tupla
     * 
     * @return dimensione della tupla
     */
    public int getLength()
    {
        return tuple.length;
    }

    /**
     * Restituisce l'item all'indice specificato.
     * 
     * @param i l'indice dell'item da restituire
     * 
     * @return l'item all'indice specificato
     * 
     * @throws ArrayIndexOutOfBoundsException se l'indice specificato non e' valido
     */
    public Item get(int i)
    {
        if(i < 0 || i >= tuple.length)
            throw new ArrayIndexOutOfBoundsException("Indice non valido.");
        
        return tuple[i];
    }

    /**
     * Aggiunge l'item specificato nella tupla all'indice specificato.
     * 
     * @param c l'item da aggiungere
     * @param i indice della tupla
     * 
     * @throws IndexOutOfBoundsException se l'indice e' minore di 0 o se supera la dimensione della tupla
     */
    public void add(Item c, int i)
    {
        if(i < 0 || i > tuple.length)
            throw new IndexOutOfBoundsException("Indice non valido.");

        tuple[i] = c;
    }

    /**
     * Determina la distanza tra la tupla riferita da {@code obj} e la tupla corrente.
     * La distanza tra due tuple e' definita come la somma delle distanze tra i rispettivi item.
     * 
     * @param obj tupla da cui calcolare la distanza
     * 
     * @throws IllegalArgumentException se le tuplem non sono della stessa lunghezza
     * 
     * @return distanza tra le due tuple
     */
    public double getDistance(Tuple obj)
    {
        if(obj.getLength() != this.getLength())
            throw new IllegalArgumentException("Le tuple devono avere stessa lunghezza.");
        
        double sumD = 0.0;
        for(int i = 0; i < tuple.length; i++)
            sumD += this.get(i).distance(obj.get(i).getValue());

        return sumD;
    }

    /**
     * Calcola la distanza media tra la tupla corrente e un insieme di tuple specificate dagli indici in {@code clusteredData}.
     * 
     * @param data il dataset contenente le tuple
     * @param clusteredData gli indici delle tuple con cui calcolare la distanza media
     * 
     * @throws IllegalArgumentException se {@code clusteredData} e' null o vuoto
     * 
     * @return distanza media tra la tupla corrente e le tuple specificate
     */
    public double avgDistance(Data data, Set<Integer> clusteredData)
    {
        if(clusteredData == null || clusteredData.size() == 0)
            throw new IllegalArgumentException("Il set non puo' essere vuoto.");

        double sumD = 0.0;
        for(Integer idx : clusteredData)
            sumD += getDistance(data.getItemSet(idx));

        return (sumD / clusteredData.size());
    }
}