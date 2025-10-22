package data;

/**
 * Classe astratta che rappresenta un item generico di un dataset.
 * <b>Questa classe deve essere estesa per creare item specifici (es. discreti o continui)</b>
 * 
 * <p>
 * Ogni item è formato da un attributo e un valore associato a quell'attributo. 
 * </p>
 * 
 * @see Attribute
 * 
 * @author Mirco Catalano
 * @author Lorenzo Amato
 */
public abstract class Item
{
    /**
     * Attrivuto riferito dall'item
     */
    private final Attribute attribute;

    /**
     * Indice dell'attributo
     */
    private final Object value;

    /**
     * Costruisce un nuovo item con il nome e l'indice specificati.
     * 
     * @param attributo attributo dell'item
     * @param index valore dell'item
     */
    protected Item(Attribute attribute, Object value)
    {
        this.attribute = attribute;
        this.value = value;
    }

    /**
     * Restituisce l'attributo riferito dall'item.
     * 
     * @return l'attributo associato all'item
     */
    public Attribute getAttribute()
    {
        return attribute;
    }

    /**
     * Restituisce il valore riferito dall'item..
     * 
     * @return valore associato all'item coerente con il dominio dell'attributo
     */
    public Object getValue()
    {
        return value;
    }

    /**
     * Restituisce una rappresentazione testuale dell'item nella forma:
     * {@code Item{attribute=..., value=...}}.
     * 
     * @return stringa che rappresenta l'item
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("Item{attribute=").append(attribute).
        append(", value=").append(value).append("}");

        return sb.toString();
    }

    /**
     * Calcola la distanza tra il valore dell'item e il valore dell'oggetto di un altro {@code Item}.
     * 
     * @param other item di cui calcolare la distanza
     * 
     * @return la distanza tra {@code this.value} e {@code other}
     */
    abstract double distance(Object a);
}
