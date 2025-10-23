package database;

import java.util.List;
import java.util.ArrayList;

/**
 * Classe che rappresenta un esempio dal database.
 * <p>
 * Ogni esempio e' costituito da una lista di oggetti che possono essere di qualsiasi tipo.
 * </p>
 * 
 * @see Comparable
 * 
 * @author Mirco Lorenzo
 * @author Lorenzo Amato
 */
public class Example implements Comparable<Example>
{
    /**
     * Array contenente oggetti dell'esempio
     */
    private List<Object> example = new ArrayList<>();

    /**
     * Aggiunge un oggetto all'esempio.
     * 
     * @param other oggetto da aggiungere
     */
    public void add(Object other)
    {
        example.add(other);
    }

    /**
     * Restituisce l'oggetto all'indice specificato.
     * 
     * @param i indice dell'oggetto
     * 
     * @return oggetto dell'insieme
     */
    public Object get(int i)
    {
        return example.get(i);
    }

    /**
     * Confronta questo esempio con un altro specificato.
     * <p>
     * Il confronto avviene in ordine lessicografico, confrontando gli oggetti
     * nelle stesse posizioni finche' non viene trovata una differenza.
     * 
     * Se tutti gli oggetti sono uguali, gli esempi sono considerati uguali.
     * </p>
     * 
     * @param other esempio da comparare
     */
    public int compareTo(Example other)
    {
        int i = 0;
        for(Object o : other.example)
        {
            if(!o.equals(this.example.get(i)))
				return ((Comparable)o).compareTo(example.get(i));

            i++;
        }

        return 0;
    }

    /**
     * Restituisce una rappresentazione testuale dell'esempio.
     * La stringa risultante contiene la rappresentazione stringa di
     * tutti gli oggetti dell'esempio, separati da spazi.
     * 
     * @return rappresentazione testuale dell'esempio.
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        for(Object o : example)
            sb.append(o.toString()).append(" ");

        return sb.toString();
    }
}