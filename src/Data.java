package src;
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
 * <p>
 * La classe fornisce metodi per ottenere il numero di esempi, il numero di attributi,
 * lo schema degli attributi e i valori dei singoli attributi.
 * </p>
 * 
 * @author Mirco Catalano
 * @author Lorenzo Amato
 * @version 1.0
 * @since 0.1
 */
public class Data {

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
    private Attribute[] attributeSet;

    /**
     * Costruttore della classe {@code Data}.
     * <p>
     * Inizializza il dataset con 14 esempi e 5 attributi discreti del dataset "Play Tennis". 
     * Vengono anche inizializzati i valori della matrice {@code data} e lo schema degli attributi {@code attributeSet}.
     * </p>
     */
    public Data() {
        numberOfExamples = 14;
        attributeSet = new Attribute[5];

        attributeSet[0] = new DiscreteAttribute("Outlook", 0, new String[] {"overcast", "rain", "sunny"});
        attributeSet[1] = new DiscreteAttribute("Temperature", 1, new String[] {"hot", "mild", "cool"});
        attributeSet[2] = new DiscreteAttribute("Humidity", 2, new String[] {"high", "normal"});
        attributeSet[3] = new DiscreteAttribute("Wind", 3, new String[] {"weak", "strong"});
        attributeSet[4] = new DiscreteAttribute("Play Tennis", 4, new String[] {"yes", "no"});

        data = new Object[][] {
            { "sunny", "hot", "high", "weak", "no" },
            { "sunny", "hot", "high", "strong", "no" },
            { "overcast", "hot", "high", "weak", "yes" },
            { "rain", "mild", "high", "weak", "yes" },
            { "rain", "cool", "normal", "weak", "yes" },
            { "rain", "cool", "normal", "strong", "no" },
            { "overcast", "cool", "normal", "strong", "yes" },
            { "sunny", "mild", "high", "weak", "no" },
            { "sunny", "cool", "normal", "weak", "yes" },
            { "rain", "mild", "normal", "weak", "yes" },
            { "sunny", "mild", "normal", "strong", "yes" },
            { "overcast", "mild", "high", "strong", "yes" },
            { "overcast", "hot", "normal", "weak", "yes" },
            { "rain", "mild", "high", "strong", "no" }
        };
    }

    /**
     * Restituisce il numero totale di esempi presenti nel dataset.
     * 
     * @return il numero di esempi
     */
    public int getNumberOfExamples() { 
        return numberOfExamples; 
    }

    /**
     * Restituisce il numero totale di attributi del dataset.
     * 
     * @return il numero di attributi
     */
    public int getNumberOfAttributes() { 
        return attributeSet.length; 
    }

    /**
     * Restituisce lo schema degli attributi del dataset.
     * 
     * @return un array contenente gli oggetti {@code Attribute}
     */
    public Attribute[] getAttributeSchema() { 
        return attributeSet; 
    }
    
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
    public Object getValue(int exampleIndex, int attributeIndex) {

        if(exampleIndex < 0 || exampleIndex > 14)
        {
            throw new ArrayIndexOutOfBoundsException("Indice fuori dai limiti.");
        }

        if(attributeIndex < 0 || attributeIndex > 5)
        {
            throw new ArrayIndexOutOfBoundsException("Indice fuori dai limiti.");
        }

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
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for(Attribute attr : attributeSet) {
            sb.append(attr.toString()).append(", ");
        }

        sb.setLength(sb.length() - 2);
        sb.append("\n");
        
        for(int i = 0; i < numberOfExamples; i++) {
            sb.append(i + 1).append(": ");
            for(int j = 0; j < attributeSet.length; j++) {
                sb.append(data[i][j]).append(", "); 
            }
            sb.setLength(sb.length() - 2);
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Metodo di test principale. Crea un oggetto {@code Data} e stampa il dataset.
     * 
     * @param args argomenti della linea di comando (non usati)
     */
    public static void main(String[] args) {
        Data data = new Data();
        System.out.println(data.toString());
    }
}