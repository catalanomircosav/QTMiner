package data;

/**
 * Rappresenta un item associato a un {@link ContinuousAttribute}.
 * <p>
 * La distanza tra due valori continui è definita come
 * la differenza assoluta tra i rispettivi valori <b>scalati in [0,1]</b>.
 * </p>
 *
 * @see Item
 * @see ContinuousAttribute
 */
public class ContinuousItem extends Item {

    /**
     * Costruisce un {@code ContinuousItem} con l'attributo e il valore specificati.
     *
     * @param attribute attributo continuo associato all'item
     * @param value valore numerico dell'item
     */
    public ContinuousItem(Attribute attribute, double value) {
        super(attribute, value);
    }

    /**
     * Calcola la distanza tra il valore di questo item e un altro valore continuo.
     * <p>
     * Formula: {@code | scaled(this.value) - scaled(other) |}
     * </p>
     *
     * @param other valore continuo con cui calcolare la distanza
     * @return distanza normalizzata (compresa tra 0 e 1)
     *
     * @throws IllegalArgumentException se il valore è {@code null} o non è un {@link Double}
     */
    @Override
    public double distance(Object other) {
        if (other == null)
            throw new IllegalArgumentException("Il valore specificato non può essere null.");

        if (!(other instanceof Double))
            throw new IllegalArgumentException("Il valore specificato deve essere di tipo Double.");

        Double thisValue = (Double) getValue();
        if (thisValue == null)
            throw new IllegalArgumentException("Il valore dell'item non può essere null.");

        ContinuousAttribute attribute = (ContinuousAttribute) getAttribute();
        double scaledOther = attribute.getScaledValue((Double) other);
        double scaledThis  = attribute.getScaledValue(thisValue);

        return Math.abs(scaledOther - scaledThis);
    }
}