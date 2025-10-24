package data;

/**
 * Rappresenta un item associato a un {@link ContinuousAttribute}.
 * <p>
 * La distanza tra due valori continui è calcolata come la differenza assoluta
 * tra i rispettivi valori normalizzati nell’intervallo {@code [0,1]}.
 * </p>
 *
 * @see Item
 * @see ContinuousAttribute
 */
public class ContinuousItem extends Item {

    /**
     * Costruisce un {@code ContinuousItem} specificando attributo continuo e valore associato.
     *
     * @param attribute l’attributo continuo associato all’item
     * @param value     il valore numerico dell’item
     */
    public ContinuousItem(Attribute attribute, double value) {
        super(attribute, value);
    }

    /**
     * Calcola la distanza tra il valore di questo item e un altro valore continuo.
     * <p>
     * La distanza è definita dalla formula:<br>
     * {@code |scaled(this.value) - scaled(other)|}
     * </p>
     *
     * @param other il valore continuo con cui calcolare la distanza; deve essere un {@link Double}
     * @return la distanza normalizzata, compresa tra {@code 0} e {@code 1}
     * @throws IllegalArgumentException se {@code other} è {@code null} o non è un {@link Double}
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
        double scaledThis = attribute.getScaledValue(thisValue);

        return Math.abs(scaledOther - scaledThis);
    }
}