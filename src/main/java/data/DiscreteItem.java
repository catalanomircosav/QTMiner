package data;

/**
 * Classe che rappresenta un elemento con un attributo discreto in un dataset.
 * <p>
 * Un {@code DiscreteItem} estende la classe astratta {@code Item} e implementa
 * il metodo astratto {@code distance} per calcolare la distanza tra due oggetti.
 * <b>La distanza è definita come 0 se i valori sono uguali e 1 se sono diversi</b>.
 * </p>
 * 
 * @see Item
 * @see DiscreteAttribute
 * 
 * @author Mirco Catalano
 * @author Lorenzo Amato
 * 
 * @version 1.0
 * @since 1.0
 */
public class DiscreteItem extends Item
{
    /**
     * Costruisce un nuovo {@code DiscreteItem} con l'attributo e il valore specificati.
     * 
     * @param attribute l'attributo discreto a cui l'item si riferisce
     * @param value valore discreto riferito dall'item
     */
    public DiscreteItem(Attribute attribute, Object value)
    {
        super(attribute, value);
    }

    /**
     * Calcola la distanza tra il valore dell'item e un altro valore discreto.
     * <p>
     * La distanza è definita come 0 se i valori sono uguali e 1 se sono diversi.
     * </p>
     * 
     * @param other il valore discreto da cui calcolare la distanza
     * 
     * @throws IllegalArgumentException se il valore specificato non è dello stesso tipo del valore dell'item.
     * @throws IllegalArgumentException se il valore specificato è {@code null}
     * @throws IllegalArgumentException se il valore dell'item è {@code null}
     * 
     * @return la distanza tra il valore dell'item e il valore specificato, compreso tra 0 e 1.
     */
    @Override
    public double distance(Object other)
    {
        if(other == null)
            throw new IllegalArgumentException("Il valore specificato non può essere null.");
        
        if(this.getValue() == null)
            throw new IllegalArgumentException("Il valore dell'item non può essere null.");
        
        if(!getValue().getClass().equals(other.getClass()))
            throw new IllegalArgumentException("Il valore specificato deve essere dello stesso tipo del valore dell'item.");
        
        return (getValue().equals(other) ? 0.0 : 1.0);
    }
}