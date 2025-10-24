package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import exceptions.EmptySetException;
import exceptions.NoValueException;
import database.TableSchema.Column;

/**
 * Fornisce metodi per estrarre dati da una tabella del database,
 * quali transazioni distinte, valori distinti di colonna e valori
 * aggregati (min/max).
 */
public class TableData {

    /** Riferimento all’accesso al database. */
    private final DBAccess db;

    /**
     * Costruisce un oggetto {@code TableData} associato al database specificato.
     *
     * @param db accesso al database già inizializzato
     */
    public TableData(DBAccess db) {
        this.db = db;
    }

    /**
     * Estrae tutte le transazioni distinte presenti nella tabella.
     * Ogni transazione è rappresentata da un {@link Example}.
     *
     * @param table nome della tabella
     * @return lista di transazioni distinte
     *
     * @throws SQLException se la query fallisce
     * @throws EmptySetException se la tabella è vuota
     */
    public List<Example> getDistinctTransactions(String table) throws SQLException, EmptySetException {
        LinkedList<Example> transSet = new LinkedList<>();
        TableSchema schema = new TableSchema(db, table);

        if (schema.getNumberOfAttributes() == 0)
            throw new SQLException("La tabella non contiene attributi.");

        StringBuilder query = new StringBuilder("SELECT DISTINCT ");
        for (int i = 0; i < schema.getNumberOfAttributes(); i++) {
            Column c = schema.getColumn(i);
            if (i > 0)
                query.append(", ");
            query.append(c.getColumnName());
        }
        query.append(" FROM ").append(table);

        try (Statement statement = db.getConnection().createStatement();
             ResultSet rs = statement.executeQuery(query.toString())) {

            boolean empty = true;

            while (rs.next()) {
                empty = false;
                Example tuple = new Example();
                for (int i = 0; i < schema.getNumberOfAttributes(); i++) {
                    Column c = schema.getColumn(i);
                    if (c.isNumber())
                        tuple.add(rs.getDouble(i + 1));
                    else
                        tuple.add(rs.getString(i + 1));
                }
                transSet.add(tuple);
            }

            if (empty) {
                throw new EmptySetException("La tabella non contiene transazioni.");
            }
        }

        return transSet;
    }

    /**
     * Estrae i valori distinti e ordinati della colonna specificata.
     *
     * @param table nome della tabella
     * @param column colonna della quale estrarre i valori
     * @return insieme ordinato dei valori distinti
     *
     * @throws SQLException se la query fallisce
     */
    public Set<Object> getDistinctColumnValues(String table, Column column) throws SQLException {
        Set<Object> valueSet = new TreeSet<>();

        String query = "SELECT DISTINCT " + column.getColumnName() +
                       " FROM " + table +
                       " ORDER BY " + column.getColumnName();

        try (Statement statement = db.getConnection().createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                if (column.isNumber())
                    valueSet.add(rs.getDouble(1));
                else
                    valueSet.add(rs.getString(1));
            }
        }

        return valueSet;
    }

    /**
     * Calcola il valore minimo o massimo di una colonna.
     *
     * @param table nome della tabella
     * @param column colonna su cui effettuare l’aggregazione
     * @param aggregate tipo di aggregazione (MIN o MAX)
     *
     * @return valore minimo o massimo trovato
     *
     * @throws SQLException se la query fallisce
     * @throws NoValueException se non esistono valori validi
     */
    public Object getAggregateColumnValue(String table, Column column, QUERY_TYPE aggregate)
            throws SQLException, NoValueException {

        String op = (aggregate == QUERY_TYPE.MAX ? "MAX" : "MIN");
        String query = "SELECT " + op + "(" + column.getColumnName() + ") FROM " + table;

        Object value = null;

        try (Statement statement = db.getConnection().createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            if (rs.next()) {
                if (column.isNumber())
                    value = rs.getFloat(1);
                else
                    value = rs.getString(1);
            }
        }

        if (value == null) {
            throw new NoValueException("Nessun valore per " + op + " sulla colonna: " + column.getColumnName());
        }

        return value;
    }
}
