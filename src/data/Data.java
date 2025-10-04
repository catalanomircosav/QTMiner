package data;

import java.util.List;
import java.util.LinkedList;

import exceptions.EmptyDatasetException;

/**
 * La classe {@code Data} rappresenta un insieme di esempi con attributi discreti, 
 * strutturati in una matrice bidimensionale. Ogni riga della matrice rappresenta 
 * un esempio, mentre ogni colonna rappresenta un attributo.
 * <p>
 * Questa implementazione contiene il dataset "Play Tennis" di esempio, con 14 
 * esempi e 5 attributi discreti: {@code Outlook}, {@code Temperature}, 
 * {@code Humidity}, {@code Wind} e {@code Play Tennis}.
 * </p>
 * 
 * @author Mirco Catalano
 * @author Lorenzo Amato
 * 
 * @version 1.2
 * @since 1.0
 */
public class Data
{

    /**
     * Matrice contenente i valori degli esempi.
     * Le righe rappresentano gli esempi e le colonne rappresentano gli attributi.
     */
    private Object[][] data;

    /**
     * Numero totale di esempi contenuti nel dataset.
     */
    private int numberOfExamples;

    /**
     * Array contenente lo schema degli attributi del dataset.
     */
    private List<Attribute> attributeSet = new LinkedList<Attribute>();

    /**
     * Costruttore della classe {@code Data}.
     * <p>
     * Inizializza il dataset con 14 esempi e 5 attributi discreti del dataset "Play Tennis". 
     * Vengono anche inizializzati i valori della matrice {@code data} e lo schema degli attributi {@code attributeSet}.
     * </p>
     */
    public Data() throws EmptyDatasetException
    {
        numberOfExamples = 14;

        // Popola lo schema con una List<Attribute>
        attributeSet.add(new DiscreteAttribute("Outlook",     0, new String[] { "overcast", "rain", "sunny" }));
        attributeSet.add(new ContinuousAttribute("Temperature",1, 0.0, 38.7));
        attributeSet.add(new DiscreteAttribute("Humidity",    2, new String[] { "high", "normal" }));
        attributeSet.add(new DiscreteAttribute("Wind",        3, new String[] { "weak", "strong" }));
        attributeSet.add(new DiscreteAttribute("Play Tennis", 4, new String[] { "yes", "no" }));

        data = new Object[][]
        {
            { "sunny", 30.3, "high", "weak", "no" },
            { "sunny", 30.3, "high", "strong", "no" },
            { "overcast", 30, "high", "weak", "yes" },
            { "rain", 13, "high", "weak", "yes" },
            { "rain", 0.0, "normal", "weak", "yes" },
            { "rain", 0.0, "normal", "strong", "no" },
            { "overcast", 0.1, "normal", "strong", "yes" },
            { "sunny", 13.0, "high", "weak", "no" },
            { "sunny", 0.1, "normal", "weak", "yes" },
            { "rain", 12.0, "normal", "weak", "yes" },
            { "sunny", 12.5, "normal", "strong", "yes" },
            { "overcast", 12.5, "high", "strong", "yes" },
            { "overcast", 29.21, "normal", "weak", "yes" },
            { "rain", 12.5, "high", "strong", "no" }
        };

        if (data.length == 0)
            throw new EmptyDatasetException();
    }
    /**
     * Restituisce il numero totale di esempi presenti nel dataset.
     * 
     * @return il numero di esempi
     */
    public int getNumberOfExamples() { return numberOfExamples; }

    /**
     * Restituisce il numero totale di attributi del dataset.
     * 
     * @return il numero di attributi
     */
    public int getNumberOfAttributes() { return attributeSet.size(); }

    /**
     * Restituisce lo schema degli attributi del dataset.
     * 
     * @return un array contenente gli oggetti {@code Attribute}
     */
    public Attribute[] getAttributeSchema() { return attributeSet.toArray(new Attribute[0]); }
    
    /**
     * Restituisce il valore di un attributo per un dato esempio.
     * 
     * @param exampleIndex l'indice dell'esempio (riga della matrice)
     * @param attributeIndex l'indice dell'attributo (colonna della matrice)
     * 
     * @return il valore dell'attributo specificato
     * 
     * @throws ArrayIndexOutOfBoundsException se gli indici sono fuori dai limiti
     */
    public Object getValue(int exampleIndex, int attributeIndex)
    {

        if(exampleIndex < 0 || exampleIndex > 14)
            throw new ArrayIndexOutOfBoundsException("Indice fuori dai limiti.");

        if(attributeIndex < 0 || attributeIndex > 5)
            throw new ArrayIndexOutOfBoundsException("Indice fuori dai limiti.");

        return data[exampleIndex][attributeIndex];
    }

    /**
     * Restituisce una rappresentazione testuale del dataset.
     * <p>
     * La prima riga contiene i nomi degli attributi separati da virgole,
     * seguita dai valori di ciascun esempio, numerati riga per riga.
     * </p>
     * 
     * @return una stringa contenente l'intero dataset in formato leggibile
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        for(Attribute attr : attributeSet)
            sb.append(attr.toString()).append(", ");

        sb.setLength(sb.length() - 2);
        sb.append("\n");
        
        for(int i = 0; i < numberOfExamples; i++)
        {
            sb.append(i).append(": ");
            for(int j = 0; j < attributeSet.size(); j++)
                sb.append(data[i][j]).append(", "); 
            
            sb.setLength(sb.length() - 2);
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Restituisce l'insieme di attributi (tupla) per l'esempio all'indice specificato.
     * 
     * @param index l'indice dell'esempio (riga della matrice)
     * 
     * @return la tupla corrispondente all'esempio
     */
    public Tuple getItemSet(int index) {
        Tuple tuple = new Tuple(attributeSet.size());
        for (int i = 0; i < attributeSet.size(); i++)
        {
            Attribute a = attributeSet.get(i);
            Object v = data[index][i];

            if (a instanceof ContinuousAttribute)
                tuple.add(new ContinuousItem(a, ((Number) v).doubleValue()), i);
            else
                tuple.add(new DiscreteItem(a, (String) v), i);
        }
        return tuple;
    }
}