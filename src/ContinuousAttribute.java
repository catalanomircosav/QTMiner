package src;
/**
 * Rappresenta un attributo continuo di un dataset.
 * <p>
 * Un attributo continuo è definito da un intervallo di valori {@code [min, max]}.
 * La classe permette di scalare qualsiasi valore dell'intervallo in un valore compreso tra 0 e 1
 * mediante il metodo {@link #getScaledValue(double)}.
 * </p>
 * 
 * @see Attribute
 * 
 */
public class ContinuousAttribute extends Attribute{

    /** Valore massimo dell'intervallo dei valori dell'attributo */
    private double min;

    /** Valore minimo dell'intervallo dei valori dell'attributo */
    private double max;

    /**
     * Costruttore della classe ContinuousAttribute
     * @param name Nome dell'attributo
     * @param index Indice dell'attributo
     * @param min Valore minimo dell'intervallo dei valori dell'attributo
     * @param max Valore massimo dell'intervallo dei valori dell'attributo
     * 
     * @throws IllegalArgumentException se min >= max
     */
    public ContinuousAttribute(String name, int index, double min, double max) {
        // Chiamata al costruttore della superclasse
        super(name, index);

        // Controllo che min sia minore di max
        if(min >= max){
            throw new IllegalArgumentException("Min must be less than max");
        }
        this.min = min;
        this.max = max;
    }

    /**
     * Restituisce il valore minimo dell'intervallo dei valori dell'attributo
     * @param value Valore da scalare
     * 
     * @return Valore minimo dell'intervallo dei valori dell'attributo
     * 
     * @throws IllegalArgumentException se value non è nell'intervallo [min, max]
     */
    public double getScaledValue(double value){
        // Controllo che il valore sia nell'intervallo [min, max]
        if(value < min || value > max){
            throw new IllegalArgumentException("Value out of range");
        }
        return (value - min) / (max - min);
    }
}