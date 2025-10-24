package database;

import java.util.List;
import java.util.ArrayList;

/**
 * Rappresenta un esempio (tupla) estratto dal database.
 * Ogni esempio è costituito da una lista di valori eterogenei
 * (numerici o stringhe), tutti confrontabili tra loro in modo
 * lessicografico.
 */
public class Example implements Comparable<Example> {

    /**
     * Lista degli oggetti che compongono l'esempio.
     */
    private final List<Object> example = new ArrayList<>();

    /**
     * Aggiunge un valore all'esempio.
     *
     * @param value valore da aggiungere
     */
    public void add(Object value) {
        example.add(value);
    }

    /**
     * Restituisce il valore alla posizione indicata.
     *
     * @param i indice del valore
     * @return valore dell'esempio
     */
    public Object get(int i) {
        return example.get(i);
    }

    /**
     * Confronta questo esempio con un altro lessicograficamente.
     * Gli oggetti sono confrontati uno ad uno tramite {@link Comparable}.
     *
     * @param other l'altro esempio da confrontare
     * @return valore negativo, zero o positivo a seconda dell'ordine
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

        // Se tutti uguali fin qui, decide la lunghezza
        return Integer.compare(this.example.size(), other.example.size());
    }

    /**
     * @return rappresentazione testuale dell'esempio con valori separati da spazio
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
