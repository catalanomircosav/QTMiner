package data;

/**
 * Rappresenta un item associato a un {@link DiscreteAttribute}.
 * <p>
 * La distanza tra due valori discreti è definita come:
 * </p>
 * <ul>
 *     <li><b>0.0</b> se i valori sono uguali</li>
 *     <li><b>1.0</b> se i valori sono diversi</li>
 * </ul>
 *
 * @see Item
 * @see DiscreteAttribute
 */
public class DiscreteItem extends Item {

    /**
     * Costruisce un {@code DiscreteItem} specificando attributo e valore discreto associato.
     *
     * @param attribute l’attributo discreto dell’item
     * @param value     il valore discreto dell’item
     */
    public DiscreteItem(Attribute attribute, Object value) {
        super(attribute, value);
    }

    /**
     * Calcola la distanza tra il valore di questo item e un altro valore discreto.
     * <p>
     * La distanza assume valore {@code 0.0} se i valori coincidono,
     * oppure {@code 1.0} in caso contrario.
     * </p>
     *
     * @param other il valore con cui calcolare la distanza
     * @return {@code 0.0} se i valori sono uguali, {@code 1.0} altrimenti
     * @throws IllegalArgumentException se {@code other} è {@code null}
     * @throws IllegalArgumentException se il valore dell’item è {@code null}
     * @throws IllegalArgumentException se {@code other} non è dello stesso tipo del valore memorizzato
     */
    @Override
    public double distance(Object other) {
        if (other == null)
            throw new IllegalArgumentException("Il valore specificato non può essere null.");
        if (getValue() == null)
            throw new IllegalArgumentException("Il valore dell'item non può essere null.");
        if (!getValue().getClass().equals(other.getClass()))
            throw new IllegalArgumentException("Il valore specificato deve essere dello stesso tipo.");

        return getValue().equals(other) ? 0.0 : 1.0;
    }
}