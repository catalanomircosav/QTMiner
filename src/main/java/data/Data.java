package data;

import java.util.List;
import java.util.LinkedList;

import exceptions.EmptyDatasetException;

/**
 * Classe che rappresenta un insieme di esempi con attributi discreti,
 * strutturati in una matrice bidimensionale.
 * 
 * <p>
 * Ogni riga della matrice rappresenta un esempio, ogni colonna un attributo.
 * 
 * Questa implementazione contiene il dataset "PlayTennis" di esempio, con 14 esempi e 5 attributi discreti.
 * </p>
 * 
 * @author Mirco Catalano
 * @author Lorenzo Amato
 */
public class Data
{
    /**
     * Matrice contenente gli esempi
     */
    private Object[][] data;

    /**
     * Numero di esempi 
     */
    private int numberOfExamples = 14;

    /**
     * Schema degli attributi
     */
    private List<Attribute> attributeSet = new LinkedList<Attribute>();

    /**
     * Costruisce il dataset con 14 esempi e 5 attributi discreti del dataset.
     * 
     * @throws EmptyDatasetException se il dataset e' vuoto
     */
    public Data() throws EmptyDatasetException
    {
        numberOfExamples = 14;

        attributeSet.add(new DiscreteAttribute("Outlook",      0, new String[] { "overcast", "rain", "sunny" }));
        attributeSet.add(new ContinuousAttribute("Temperature",1, 3.2, 38.7));
        attributeSet.add(new DiscreteAttribute("Humidity",     2, new String[] { "high", "normal" }));
        attributeSet.add(new DiscreteAttribute("Wind",         3, new String[] { "weak", "strong" }));
        attributeSet.add(new DiscreteAttribute("Play Tennis",  4, new String[] { "yes", "no" }));

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
     * Restituisce il numero totale di esempi presenti nel dataset
     * 
     * @return numero di esempi
     */
    public int getNumberOfExamples()
    {
        return numberOfExamples;
    }

    /**
     * Restituisce il numero totale di attributi del dataset
     * 
     * @return il numero di attributi
     */
    public int getNumberOfAttributes()
    {
        return attributeSet.size();
    }

    /**
     * Restituisce lo schema degli attributi del dataset
     * 
     * @return array contenente gli attributi
     */
    public Attribute[] getAttributeSchema()
    {
        return attributeSet.toArray(new Attribute[0]);
    }

    /**
     * Restituisce il valroe di un attributo per gli indici specificati
     * 
     * @param exampleIndex indice dell'esempio
     * @param attributeIndex indice dell'attributo
     * 
     * @return valore dell'attributo agli indici specificati
     * 
     * @throws ArrayIndexOutOfBoundsException se gli indici son ofuori dai limiti
     */
    public Object getValue(int exampleIndex, int attributeIndex)
    {
        if ((exampleIndex < 0 || exampleIndex > 14) 
            || ((attributeIndex < 0 || attributeIndex > 5)))
                throw new ArrayIndexOutOfBoundsException("Indici fuori dai limiti.");

        
        return data[exampleIndex][attributeIndex];
    }

    /**
     * Restituisce una tupla di item all'indice specificato.
     * 
     * @param index l'indice dell'esempio
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
    
    /**
     * Restituisce una rappresentazione testuale del dataset.
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
}
