package database;

/**
 * Enumerazione che rappresenta le operazioni di aggregazione applicabili
 * su una colonna del database (valore minimo o massimo).
 * <p>
 * Viene utilizzata, ad esempio, nel metodo
 * {@link database.TableData#getAggregateColumnValue(String, database.TableSchema.Column, QUERY_TYPE)}.
 * </p>
 *
 * <ul>
 *   <li>{@code MIN} — calcola il valore minimo della colonna</li>
 *   <li>{@code MAX} — calcola il valore massimo della colonna</li>
 * </ul>
 */
public enum QUERY_TYPE {

    /** Operazione di aggregazione che restituisce il valore minimo. */
    MIN,

    /** Operazione di aggregazione che restituisce il valore massimo. */
    MAX
}