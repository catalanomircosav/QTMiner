package data;

import java.io.Serializable;

/**
 * Classe astratta che rappresenta un item generico di un dataset.
 * <p>
 * Un item è definito da un {@link Attribute} e da un valore
 * appartenente al dominio di tale attributo. Le sottoclassi
 * implementano il metodo {@link #distance(Object)} per definire
 * la distanza tra valori (ad esempio 1/0 per attributi discreti,
 * o distanza numerica scalata per quelli continui).
 * </p>
 *
 * @see Attribute
 */
public abstract class Item implements Serializable {

    /** Attributo associato all'item. */
    private final Attribute attribute;

    /** Valore dell'item, coerente con il dominio dell'attributo. */
    private final Object value;

    /**
     * Costruisce un nuovo item con attributo e valore specificati.
     *
     * @param attribute attributo di riferimento dell'item
     * @param value     valore associato all'attributo
     */
    protected Item(Attribute attribute, Object value) {
        this.attribute = attribute;
        this.value = value;
    }

    /**
     * @return l'attributo riferito dall'item
     */
    public Attribute getAttribute() {
        return attribute;
    }

    /**
     * @return il valore associato all'item
     */
    public Object getValue() {
        return value;
    }

    /**
     * Rappresentazione testuale dell'item nella forma:
     * {@code Item{attribute=..., value=...}}.
     */
    @Override
    public String toString() {
        return "Item{attribute=" + attribute + ", value=" + value + "}";
    }

    /**
     * Calcola la distanza tra il valore di questo item e quello specificato.
     *
     * @param other valore con cui confrontare {@code this.value}
     * @return distanza tra i due valori (definita dalle sottoclassi)
     */
    public abstract double distance(Object other);
}