package data;

import java.util.TreeSet;
import java.util.Iterator;

/**
 * Classe che rappresenta un attributo discreto in un dataset.
 * <p>
 * Un attributo discreto è definito da un insieme di valori distinti.
 * 
 * La classe permette di ottenere il valore in base all'indice e il numero totale di valori distinti disponibili.
 * </p>
 * 
 * @see Attribute
 * 
 * @author Lorenzo Amato
 * @author Mirco Catalano
 * 
 * @version 1.0
 * @since 1.0
 */
public class DiscreteAttribute extends Attribute implements Iterable<String>
{
    /**
     * Contenitore per i valori distinti dell'attributo discreto.
     */
    private TreeSet<String> values;

    /**
     * Costruisce un nuovo attributo con il nome, l'indice e l'insieme dei valori possibili.
     * 
     * @param name nome dell'attributo
     * @param index indice dell'attributo
     * @param values valori distinti dell'attributo
     * 
     * @throws IllegalArgumentException se {@code values} è {@code null} o vuoto.
     */
    public DiscreteAttribute(String name, int index, String[] values)
    {
        super(name, index);

        if(values == null || values.length == 0)
            throw new IllegalArgumentException("Valori non validi.");
        
        this.values = new TreeSet<String>();
        for(String v : values) this.values.add(v);
    }

    /**
     * Restituisce il numero di valori distinti che l'attributo può assumere.
     * 
     * @return numero di valori distinti disponibili
     */
    public int getNumberOfDistinctValues()
    {
        return values.size();
    }

    /**
     * Restituisce un iteratore sugli elementi di {@code values}.
     */
    public Iterator<String> iterator()
    {
        return values.iterator();
    }
}
