package data;

import java.io.Serializable;

/**
 * Classe astratta che rappresenta un item generico di un dataset.
 * <p>
 * Un item è costituito da un {@link Attribute} e da un valore appartenente al
 * dominio di tale attributo. Le sottoclassi concretizzano il metodo
 * {@link #distance(Object)} per definire la misura di distanza tra due valori,
 * ad esempio distanza numerica scalata per attributi continui o valore
 * binario (0/1) per attributi discreti.
 * </p>
 *
 * @see Attribute
 */
public abstract class Item implements Serializable {

    /** Attributo associato all'item. */
    private final Attribute attribute;

    /** Valore dell'item, appartenente al dominio dell'attributo. */
    private final Object value;

    /**
     * Costruisce un nuovo item specificando l'attributo e il valore associato.
     *
     * @param attribute l'attributo di riferimento
     * @param value     il valore dell'item
     */
    protected Item(Attribute attribute, Object value) {
        this.attribute = attribute;
        this.value = value;
    }

    /**
     * Restituisce l'attributo associato a questo item.
     *
     * @return l'attributo dell'item
     */
    public Attribute getAttribute() {
        return attribute;
    }

    /**
     * Restituisce il valore associato a questo item.
     *
     * @return il valore dell'item
     */
    public Object getValue() {
        return value;
    }

    /**
     * Restituisce una rappresentazione testuale dell'item nella forma:
     * {@code Item{attribute=..., value=...}}.
     *
     * @return la stringa rappresentativa dell'item
     */
    @Override
    public String toString() {
        return "Item{attribute=" + attribute + ", value=" + value + "}";
    }

    /**
     * Calcola la distanza tra il valore di questo item e quello specificato.
     * <p>
     * L'effettiva metrica di distanza è definita nelle sottoclassi.
     * </p>
     *
     * @param other il valore con cui confrontare {@code this.value}
     * @return la distanza tra i due valori
     */
    public abstract double distance(Object other);
}