package data;

/**
 * La classe {@code DiscreteItem} rappresenta un elemento di un attributo discreto in un dataset.
 * <p>
 * Un {@code DiscreteItem} estende la classe astratta {@code Item} e implementa il metodo {@code distance} per calcolare la distanza tra il valore dell'item e un altro valore discreto.
 * La distanza è definita come 0 se i valori sono uguali e 1 se sono diversi.
 * </p>
 * 
 * @author Mirco Catalano
 * @author Lorenzo Amato
 * 
 * @version 1.0
 * @since 2.0
 * 
 * @see Item
 * @see DiscreteAttribute
 */
public class DiscreteItem extends Item
{
    /**
     * Costruisce un nuovo {@code DiscreteItem} con l'attributo e il valore specificati.
     * 
     * @param attribute l'attributo discreto a cui l'item si riferisce
     * @param value     il valore discreto dell'item
     * 
     * @throws IllegalArgumentException se l'attributo non è di tipo {@code DiscreteAttribute}
     */
    public DiscreteItem(Attribute attribute, Object value)
    {
        if (!(attribute instanceof DiscreteAttribute))
            throw new IllegalArgumentException("L'attributo deve essere di tipo DiscreteAttribute.");

        super(attribute, value);
    }

    /**
     * Calcola la distanza tra il valore dell'item e un altro valore discreto.
     * <p>
     * La distanza è definita come 0 se i valori sono uguali e 1
     * se sono diversi.
     * </p>
     * 
     * @param a il valore discreto con cui calcolare la distanza
     * 
     * @throws IllegalArgumentException se il valore specificato non è dello stesso tipo del valore dell'item
     * @throws IllegalArgumentException se il valore specificato è {@code null}
     * @throws IllegalArgumentException se il valore dell'item è {@code null}
     *
     * @return la distanza tra il valore dell'item e il valore specificato (0 o 1)
     */
    @Override
    public double distance(Object a) 
    {
        if (a == null) 
            throw new IllegalArgumentException("Il valore specificato non può essere null.");
            
        if (getValue() == null)
            throw new IllegalArgumentException("Il valore dell'item non può essere null.");

        if (!getValue().getClass().equals(a.getClass()))
            throw new IllegalArgumentException("Il valore specificato deve essere dello stesso tipo del valore dell'item.");

        return (getValue().equals(a) ? 0.0 : 1.0);
    }
}
