package data;

/**
 * Classe che rappresenta un elemento con attributo continuo in un dataset.
 * <p>
 * Un {@code ContinuousItem} estende la classe astratta {@code Item} e implementa
 * il metodo astratto {@code distance} per calcolare la distanza tra due oggetti.
 * <b>La distanza è definita come il valore assoluto della differenza tra due valori scalati</b>.
 * </p>
 * 
 * @see Item
 * @see ContinuousItem
 * 
 * @author Mirco Catalano
 * @author Lorenzo Amato
 */
public class ContinuousItem extends Item
{
    /**
     * Costruisce un nuovo {@code ContinuousItem} con l'attributo e il valore specificati.
     * 
     * @param attribute l'attributo continuo riferito dall'item.
     * @param value il valore contiuo dell'item 
     */
    public ContinuousItem(Attribute attribute, double value)
    {
        super(attribute, value);
    }

    /**
     * Calcola la distanza tra il valore dell'item e un altro valore continuo.
     * <p>
     * La distanza è calcolata come il valore assoluto della differenza tra i due valori,
     * dopo averli scalati in base ai parametri di scaling.
     * </p>
     * 
     * @param other valore continuo di cui calcolare la distanza.
     * 
     * @return la distanza tra il valore dell'item e il valore specificato.
     */
    @Override
    public double distance(Object other)
    {
        if(other == null)
            throw new IllegalArgumentException("Il valore specificato non può essere null.");
        
        if(getValue() == null)
            throw new IllegalArgumentException("Il valore dell'item non può essere null.");
        
        if(!getValue().getClass().equals(other.getClass()))
            throw new IllegalArgumentException("Il valore specificato deve essere dello stesso tipo del valore dell'item.");
        
        return Math.abs(
            ((ContinuousAttribute) getAttribute()).getScaledValue((Double) other) -
            ((ContinuousAttribute) getAttribute()).getScaledValue((Double) getValue())
        );
    }
}