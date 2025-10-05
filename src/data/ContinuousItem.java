package data;

/**
 * La classe {@code ContinuousItem} rappresenta un elemento di un attributo continuo in un dataset.
 * <p>
 * Un {@code ContinuousItem} estende la classe astratta {@code Item} e implementa il metodo {@code distance} per calcolare la distanza tra il valore dell'item e un altro valore continuo.
 * La distanza è calcolata come il valore assoluto della differenza tra i due valori, dopo averli scalati in base ai parametri di scaling dell'attributo continuo.
 * </p>
 * 
 * @author Mirco Catalano
 * @author Lorenzo Amato
 * 
 * @version 1.0
 * @since 6.0
 * 
 * @see Item
 * @see ContinuousAttribute
 */
public class ContinuousItem extends Item
{   
    /**
     * Costruisce un nuovo {@code ContinuousItem} con l'attributo e il valore specificati.
     * 
     * @param attribute l'attributo continuo a cui l'item si riferisce
     * @param value il valore continuo dell'item
     */
    public ContinuousItem(Attribute attribute, double value)
    {
        super(attribute, value);
    }

    /**
     * Calcola la distanza tra il valore dell'item e un altro valore continuo.
     * 
     * <p>
     * La distanza è calcolata come il valore assoluto della differenza tra i due
     * valori, dopo averli scalati in base ai parametri di scaling dell'attributo continuo.
     * </p>
     * 
     * @param a il valore continuo con cui calcolare la distanza
     * 
     * @return la distanza tra il valore dell'item e il valore specificato
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

        return Math.abs(
            ((ContinuousAttribute) getAttribute()).getScaledValue((Double) a) - 
            ((ContinuousAttribute) getAttribute()).getScaledValue((Double) getValue()) 
        );
    }
}