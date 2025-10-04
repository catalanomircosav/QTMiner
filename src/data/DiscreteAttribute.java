package data;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * Rappresenta un attributo discreto di un dataset.
 * <p>
 * Un attributo discreto può assumere solo un insieme finito di valori distinti.
 * Questa classe estende {@link Attribute} e memorizza tutti i possibili valori
 * che l'attributo può assumere.
 * </p>
 * 
 * <p>
 * Fornisce metodi per ottenere il valore in base all'indice e il numero totale
 * di valori distinti disponibili.
 * </p>
 * 
 * @see Attribute
 * 
 * @author Lorenzo Amato
 * @author Mirco Catalano
 * 
 * @version 2.0
 * @since 1.0
 */
public class DiscreteAttribute extends Attribute implements Iterable<String>
{

    /** Contenitore di tipo TreeSet per i valori distinti dell'attributo discreto. */
    private TreeSet<String> values;

    /**
     * Costruisce un nuovo attributo discreto con il nome, l'indice e l'insieme dei valori possibili.
     * 
     * @param name nome dell'attributo; non può essere {@code null} o vuoto
     * @param index indice dell'attributo; deve essere maggiore o uguale a 0
     * @param values array contenente i valori distinti dell'attributo; non può
     * essere {@code null} o vuoto
     * 
     * @throws IllegalArgumentException se {@code values} è {@code null} o vuoto
     */
    public DiscreteAttribute(String name, int index, String[] values)
    {
        super(name, index);

        if (values == null || values.length == 0)
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

    public Iterator <String> iterator()
    {
        return values.iterator();
    }
}