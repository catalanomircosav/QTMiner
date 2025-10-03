package src;

/**
 * Classe astratta che rappresenta un item, ossia una coppia formata da un 
 * {@link Attribute} (attributo) e da un valore osservato per quell'attributo.
 * <p>
 * Ogni sottoclasse di {@code Item} implementa un diverso tipo di calcolo della
 * distanza tra valori, a seconda della natura dell'attributo (ad esempio discreto
 * o continuo).
 * </p>
 */
public abstract class Item {
    /** Attributo a cui l'item si riferisce */
    Attribute attribute;

    /** Valore associato all'attributo */
    Object value;

    /**
     * Costruisce un item a partire da un attributo e da un valore.
     *
     * @param attribute l'attributo a cui l'item si riferisce
     * @param value il valore dell'item, coerente con il dominio dell'attributo
     */
    public Item(Attribute attribute, Object value) {
        this.attribute = attribute;
        this.value = value;
    }

    /**
     * Restituisce l'attributo a cui l'item si riferisce.
     *
     * @return l'attributo associato all'item
     */
    public Attribute getAttribute() {
        return attribute;
    }

    /**
     * Restituisce il valore dell'item.
     *
     * @return il valore osservato per l'attributo
     */
    public Object getValue() {
        return value;
    }

    /**
     * Restituisce una rappresentazione testuale dell'item nella forma:
     * {@code Item{attribute=..., value=...}}.
     *
     * @return stringa descrittiva dell'item
     */
    @Override
    public String toString() {
        return "Item{" +
                "attribute=" + attribute +
                ", value=" + value +
                '}';
    }

    /**
     * Calcola la distanza tra il valore dell'item e un altro oggetto.
     *
     * @param a il valore rispetto al quale calcolare la distanza
     * @return la distanza tra {@code this.value} e {@code a}
     */
    abstract double distance(Object a);
}