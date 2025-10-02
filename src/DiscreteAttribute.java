package src;
/**
 * Rappresenta un attributo discreto di un dataset.
 * <p>
 * Un attributo discreto può assumere solo un insieme finito di valori distinti.
 * Questa classe estende {@link Attribute} e memorizza tutti i possibili valori
 * che l'attributo può assumere.
 * </p>
 * 
 * <p>
 * Fornisce metodi per ottenere il valore in base all'indice e il numero totale
 * di valori distinti disponibili.
 * </p>
 * 
 * @see Attribute
 * 
 * @author Lorenzo Amato
 * @author Mirco Catalano
 * 
 * @version 1.0
 * @since 1.0
 */
public class DiscreteAttribute extends Attribute {

    /** Array contenente tutti i valori possibili dell'attributo */
    private String[] values;

    /**
     * Costruisce un nuovo attributo discreto con il nome, l'indice e l'insieme dei valori possibili.
     * 
     * @param name nome dell'attributo; non può essere {@code null} o vuoto
     * @param index indice dell'attributo; deve essere maggiore o uguale a 0
     * @param values array contenente i valori distinti dell'attributo; non può essere {@code null} o vuoto
     * 
     * @throws IllegalArgumentException se {@code values} è {@code null} o vuoto
     */
    public DiscreteAttribute(String name, int index, String[] values) {
        super(name, index);

        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Valori non validi.");
        }

        this.values = values;
    }

    /**
     * Restituisce il valore dell'attributo all'indice specificato.
     * 
     * @param i indice del valore da restituire
     * @return il valore dell'attributo corrispondente all'indice
     * @throws ArrayIndexOutOfBoundsException se l'indice non è valido
     */
    public String getValue(int i) {
        return values[i];
    }

    /**
     * Restituisce il numero di valori distinti che l'attributo può assumere.
     * 
     * @return numero di valori distinti disponibili
     */
    public int getNumberOfDistinctValues() {
        return values.length;
    }
}