package data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import database.DBAccess;
import database.Example;
import database.QUERY_TYPE;
import database.TableData;
import database.TableSchema;
import database.TableSchema.Column;
import exceptions.DatabaseConnectionException;
import exceptions.EmptyDatasetException;
import exceptions.EmptySetException;
import exceptions.NoValueException;

/**
 * Rappresenta un dataset composto da una lista di esempi (righe) e da una lista
 * di attributi (colonne) estratti da una tabella del database.
 * <p>
 * La classe fornisce metodi per accedere ai valori grezzi del dataset e per
 * ottenere tuple di {@link Item}, consentendo così successive elaborazioni
 * (es. clustering).
 * </p>
 */
public class Data {

    /** Lista degli esempi (righe del dataset). */
    private final List<Example> data = new ArrayList<>();

    /** Lista degli attributi dello schema (colonne del dataset). */
    private final List<Attribute> attributeSet = new ArrayList<>();

    /** Numero totale di esempi nel dataset. */
    private int numberOfExamples;

    /**
     * Costruisce un dataset a partire dal contenuto di una tabella del database.
     *
     * @param tableName il nome della tabella da cui leggere i dati
     * @throws DatabaseConnectionException se non è possibile stabilire la connessione al database
     * @throws EmptyDatasetException       se la tabella non contiene alcuna riga
     * @throws SQLException                se si verificano errori SQL durante lettura o chiusura connessione
     */
    public Data(String tableName)
            throws DatabaseConnectionException, EmptyDatasetException, SQLException {

        DBAccess databaseAccess = new DBAccess();

        try {
            databaseAccess.initConnection();

            TableSchema tableSchema = new TableSchema(databaseAccess, tableName);
            createAttributesFromTableSchema(tableName, databaseAccess, tableSchema);

            TableData tableData = new TableData(databaseAccess);
            try {
                List<Example> transactions = tableData.getDistinctTransactions(tableName);

                if (transactions.isEmpty())
                    throw new EmptyDatasetException("La tabella " + tableName + " è vuota.");

                data.addAll(transactions);
                numberOfExamples = data.size();
            }
            catch (EmptySetException e) {
                throw new EmptyDatasetException("La tabella " + tableName + " è vuota.");
            }
        }
        finally {
            try {
                databaseAccess.closeConnection();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Restituisce il numero di esempi presenti nel dataset.
     *
     * @return il numero di esempi
     */
    public int getNumberOfExamples() {
        return numberOfExamples;
    }

    /**
     * Restituisce il numero di attributi dello schema del dataset.
     *
     * @return il numero di attributi
     */
    public int getNumberOfAttributes() {
        return attributeSet.size();
    }

    /**
     * Restituisce l'intero schema degli attributi del dataset.
     *
     * @return un array contenente tutti gli attributi
     */
    public Attribute[] getAttributeSchema() {
        return attributeSet.toArray(new Attribute[0]);
    }

    /**
     * Restituisce il valore grezzo di una cella del dataset, specificata tramite
     * indici di esempio e attributo.
     *
     * @param exampleIndex   indice dell'esempio (riga)
     * @param attributeIndex indice dell'attributo (colonna)
     * @return il valore corrispondente nella cella indicata
     * @throws ArrayIndexOutOfBoundsException se gli indici non sono validi
     */
    public Object getValue(int exampleIndex, int attributeIndex) {
        if (exampleIndex < 0 || exampleIndex >= numberOfExamples ||
            attributeIndex < 0 || attributeIndex >= attributeSet.size()) {
            throw new ArrayIndexOutOfBoundsException("Indici fuori dai limiti.");
        }
        return data.get(exampleIndex).get(attributeIndex);
    }

    /**
     * Restituisce una tupla di {@link Item} contenente i valori dell’esempio
     * specificato.
     *
     * @param index l’indice dell’esempio di cui creare la tupla
     * @return la tupla corrispondente all’esempio
     */
    public Tuple getItemSet(int index) {
        Tuple tuple = new Tuple(attributeSet.size());
        for (int i = 0; i < attributeSet.size(); i++) {
            Attribute a = attributeSet.get(i);
            Object v = data.get(index).get(i);

            if (a instanceof ContinuousAttribute)
                tuple.add(new ContinuousItem(a, ((Number) v).doubleValue()), i);
            else
                tuple.add(new DiscreteItem(a, v.toString()), i);
        }
        return tuple;
    }

    /**
     * Restituisce una rappresentazione testuale del dataset, comprensiva
     * dell’intestazione e di tutte le righe di dati.
     *
     * @return stringa rappresentativa del dataset
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Attribute attr : attributeSet)
            sb.append(attr.toString()).append(", ");
        sb.setLength(sb.length() - 2);
        sb.append("\n");

        for (int i = 0; i < numberOfExamples; i++) {
            sb.append(i).append(": ");
            for (int j = 0; j < attributeSet.size(); j++)
                sb.append(data.get(i).get(j)).append(", ");
            sb.setLength(sb.length() - 2);
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Crea gli attributi del dataset analizzando lo schema della tabella
     * del database. Gli attributi numerici vengono mappati su
     * {@link ContinuousAttribute}, mentre gli attributi non numerici su
     * {@link DiscreteAttribute}.
     *
     * @param tableName  il nome della tabella
     * @param database   l’accesso al database
     * @param tableSchema lo schema della tabella
     * @throws SQLException se si verificano errori SQL durante la lettura
     */
    private void createAttributesFromTableSchema(String tableName, DBAccess database, TableSchema tableSchema)
            throws SQLException {

        TableData tableData = new TableData(database);

        for (int i = 0; i < tableSchema.getNumberOfAttributes(); i++) {
            Column column = tableSchema.getColumn(i);
            String columnName = column.getColumnName();

            if (column.isNumber()) {
                try {
                    Object minObj = tableData.getAggregateColumnValue(tableName, column, QUERY_TYPE.MIN);
                    Object maxObj = tableData.getAggregateColumnValue(tableName, column, QUERY_TYPE.MAX);

                    double min = Double.parseDouble(minObj.toString());
                    double max = Double.parseDouble(maxObj.toString());

                    attributeSet.add(new ContinuousAttribute(columnName, i, min, max));
                }
                catch (NoValueException e) {
                    System.err.println(e.getMessage());
                }
            }
            else {
                Set<Object> distinctValues = tableData.getDistinctColumnValues(tableName, column);

                String[] values = distinctValues.stream()
                        .map(Object::toString)
                        .toArray(String[]::new);

                attributeSet.add(new DiscreteAttribute(columnName, i, values));
            }
        }
    }
}