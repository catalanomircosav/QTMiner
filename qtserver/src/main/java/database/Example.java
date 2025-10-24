package database;

import java.util.List;
import java.util.ArrayList;

/**
 * Rappresenta un esempio (tupla) estratto dal database.
 * <p>
 * Ogni esempio è costituito da una lista ordinata di valori eterogenei
 * (numerici o stringhe), confrontabili tra loro in modo lessicografico.
 * La classe fornisce metodi per aggiungere e recuperare valori e implementa
 * il confronto tra esempi tramite ordinamento lessicografico.
 * </p>
 */
public class Example implements Comparable<Example> {

    /** Lista dei valori che compongono l'esempio. */
    private final List<Object> example = new ArrayList<>();

    /**
     * Costruisce un esempio vuoto.
     */
    public Example() { }

    /**
     * Aggiunge un valore all'esempio.
     *
     * @param value il valore da aggiungere
     */
    public void add(Object value) {
        example.add(value);
    }

    /**
     * Restituisce il valore alla posizione indicata.
     *
     * @param i l’indice del valore da restituire
     * @return il valore memorizzato nella posizione specificata
     * @throws IndexOutOfBoundsException se {@code i} non è un indice valido
     */
    public Object get(int i) {
        return example.get(i);
    }

    /**
     * Confronta questo esempio con un altro esempio in modo lessicografico.
     * <p>
     * I valori vengono confrontati uno alla volta tramite {@link Comparable};
     * se tutti i valori coincidono fino alla lunghezza minima, l’ordine finale
     * è determinato dal numero di valori contenuti nelle due tuple.
     * </p>
     *
     * @param other l’altro esempio da confrontare
     * @return un valore negativo, zero o positivo in base all’ordine determinato
     */
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public int compareTo(Example other) {
        int size = Math.min(this.example.size(), other.example.size());

        for (int i = 0; i < size; i++) {
            Object o1 = this.example.get(i);
            Object o2 = other.example.get(i);

            if (!o1.equals(o2)) {
                return ((Comparable) o1).compareTo(o2);
            }
        }

        return Integer.compare(this.example.size(), other.example.size());
    }

    /**
     * Restituisce la rappresentazione testuale dell'esempio,
     * con i valori separati da spazio.
     *
     * @return la stringa che rappresenta l'esempio
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Object o : example)
            sb.append(o.toString()).append(" ");

        if (!example.isEmpty())
            sb.setLength(sb.length() - 1);

        return sb.toString();
    }
}