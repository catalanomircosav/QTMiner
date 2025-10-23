package database;

/**
 * Enumerazione che rappresenta le tipologie di operazioni di aggregazione
 * che possono essere eseguite su una colonna di una tabella del database.
 * <p>
 * Le costanti di questa enumerazione vengono utilizzate per specificare
 * l'operatore SQL di aggregazione da applicare in metodi come 
 * {@link database.TableData#getAggregateColumnValue(String, Column, QUERY_TYPE)}.
 * </p>
 * 
 * <ul>
 *   <li>{@code MIN}: indica il calcolo del valore minimo nella colonna.</li>
 *   <li>{@code MAX}: indica il calcolo del valore massimo nella colonna.</li>
 * </ul>
 * 
 * @author Mirco Catalano
 * @author Lorenzo Amato
 */
public enum QUERY_TYPE
{
    /**
     * Operatore di aggregazione che restituisce il valore minimo
     * della colonna selezionata.
     */
    MIN,

    /**
     * Operatore di aggregazione che restituisce il valore massimo
     * della colonna selezionata.
     */
    MAX
}
