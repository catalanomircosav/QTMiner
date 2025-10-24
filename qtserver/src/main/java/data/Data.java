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
 * Rappresenta un dataset composto da una lista di esempi (righe)
 * e una lista di attributi (colonne).
 */
public class Data {

    /** Lista degli esempi (righe del dataset). */
    private final List<Example> data = new ArrayList<>();

    /** Lista degli attributi dello schema. */
    private final List<Attribute> attributeSet = new ArrayList<>();

    /** Numero totale di esempi nel dataset. */
    private int numberOfExamples;

    /**
     * Costruisce un dataset a partire da una tabella del database.
     *
     * @param tableName nome della tabella
     *
     * @throws DatabaseConnectionException se non è possibile connettersi
     * @throws EmptyDatasetException se la tabella è vuota
     * @throws SQLException se avvengono errori SQL
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

    /** @return numero di esempi nel dataset */
    public int getNumberOfExamples() {
        return numberOfExamples;
    }

    /** @return numero di attributi nel dataset */
    public int getNumberOfAttributes() {
        return attributeSet.size();
    }

    /** @return array degli attributi */
    public Attribute[] getAttributeSchema() {
        return attributeSet.toArray(new Attribute[0]);
    }

    /**
     * Restituisce il valore (grezzo) di una cella del dataset.
     *
     * @param exampleIndex indice dell'esempio
     * @param attributeIndex indice dell'attributo
     *
     * @return il valore della cella
     *
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
     * Restituisce una tupla (insieme di Item) dell'esempio indicato.
     *
     * @param index indice dell'esempio
     * @return tupla corrispondente
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // intestazione
        for (Attribute attr : attributeSet)
            sb.append(attr.toString()).append(", ");
        sb.setLength(sb.length() - 2);
        sb.append("\n");

        // righe
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
     * Crea gli attributi a partire dallo schema della tabella.
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