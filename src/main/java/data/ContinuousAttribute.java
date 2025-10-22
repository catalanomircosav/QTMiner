package data;

/**
 * Classe che rappresenta un attributo continuo in un dataset.
 * <p>
 * Un attributo continuo è definito da un intervallo di valori {@code [min, max]}.
 * 
 * La classe permette di scalare qualsiasi valore dell'intervallo in un valore
 * compreso tra 0 e 1 mediante l'utilizzo del metodo {@link #getScaledValue(double)}.
 * </p>
 * 
 * @see Attribute
 * 
 * @author Lorenzo Amato
 * @author Mirco Catalano
 */
public class ContinuousAttribute extends Attribute
{
    /**
     * Valore massimo dell'intervallo di valori dell'attributo.
     */
    private double min;

    /**
     * Valore minimo dell'intervallo dei valori dell'attributo.
     */
    private double max;

    /**
     * Costruisce un nuovo attributo continuo con il nome, l'indice, il valore minimo e massimo specificati.
     * 
     * @param name nome dell'attributo
     * @param index indice dell'attributo
     * @param min valore minimo del dominio dell'attributo
     * @param max valore massimo del dominio dell'attributo
     * 
     * @throws IllegalArgumentException se {@code min} è maggiore o uguale a {@code max}.
     */
    public ContinuousAttribute(String name, int index, double min, double max)
    {
        super(name, index);
        
        if(min >= max)
            throw new IllegalArgumentException("Il valore minimo non può essere >= del valore massimo.");

        this.min = min;
        this.max = max;
    }

    /**
     * Restituisce il valore scalato, compreso tra 0 e 1, nel dominio dell'attributo.
     * 
     * @param value valore da scalare nel dominio dell'attributo.
     * 
     * @return valore scalato nel dominio dell'attributo.
     * 
     * @throws IllegalArgumentException se {@code value} non è nel dominio {@code [min, max]}.  
    */
    public double getScaledValue(double value)
    {
        if(value < this.min || value > this.max)
            throw new IllegalArgumentException("Il valore non può essere fuori dal dominio.");

        return (value - this.min) / (this.max - this.min);
    }
}