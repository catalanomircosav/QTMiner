package data;

/**
 * Rappresenta un attributo continuo di un dataset.
 * <p>
 * Un attributo continuo è definito da un intervallo chiuso
 * {@code [min, max]}. Il metodo {@link #getScaledValue(double)}
 * consente di normalizzare un valore appartenente a questo intervallo
 * in un numero compreso tra 0 e 1.
 * </p>
 *
 * @see Attribute
 */
public class ContinuousAttribute extends Attribute {

    /** Valore minimo dell'intervallo dell'attributo. */
    private final double min;

    /** Valore massimo dell'intervallo dell'attributo. */
    private final double max;

    /**
     * Costruisce un attributo continuo specificando intervallo e posizione.
     *
     * @param name  nome dell'attributo
     * @param index indice dell'attributo nello schema (>= 0)
     * @param min   valore minimo del dominio
     * @param max   valore massimo del dominio
     *
     * @throws IllegalArgumentException se {@code min >= max}
     */
    public ContinuousAttribute(String name, int index, double min, double max) {
        super(name, index);

        if (min >= max) {
            throw new IllegalArgumentException(
                "Il valore minimo deve essere inferiore al valore massimo."
            );
        }

        this.min = min;
        this.max = max;
    }

    /**
     * Calcola il valore scalato del parametro in un intervallo
     * normalizzato {@code [0, 1]}.
     *
     * @param value valore da scalare, compreso nell'intervallo {@code [min, max]}
     * @return valore normalizzato tra 0 e 1
     */
    public double getScaledValue(double value) {
        return (value - min) / (max - min);
    }

    /**
     * @return valore minimo del dominio dell'attributo
     */
    public double getMin() {
        return min;
    }

    /**
     * @return valore massimo del dominio dell'attributo
     */
    public double getMax() {
        return max;
    }
}