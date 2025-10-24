package database;

import java.util.List;
import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.DatabaseMetaData;

/**
 * Rappresenta lo schema di una tabella presente in un database relazionale.
 * <p>
 * Questa classe estrae i metadati della tabella tramite JDBC e converte
 * i tipi SQL in tipologie semplificate utilizzate all'interno
 * dell'applicazione (ad esempio {@code "number"} o {@code "string"}).
 * </p>
 *
 * @see Column
 */
public class TableSchema {

    /**
     * Rappresenta una singola colonna dello schema della tabella.
     * <p>
     * Ogni colonna è descritta dal nome e da un tipo semplificato che
     * indica se i valori sono numerici o testuali.
     * </p>
     */
    public class Column {

        /** Nome della colonna. */
        private final String name;

        /** Tipo semplificato associato alla colonna ({@code "number"} o {@code "string"}). */
        private final String type;

        /**
         * Costruisce una colonna dello schema specificando nome e tipo.
         *
         * @param name il nome della colonna
         * @param type il tipo semplificato ({@code "number"} o {@code "string"})
         */
        Column(String name, String type) {
            this.name = name;
            this.type = type;
        }

        /**
         * Restituisce il nome della colonna.
         *
         * @return il nome della colonna
         */
        public String getColumnName() {
            return name;
        }

        /**
         * Verifica se la colonna contiene valori numerici.
         *
         * @return {@code true} se i valori della colonna sono numerici, {@code false} altrimenti
         */
        public boolean isNumber() {
            return type.equals("number");
        }

        /**
         * Restituisce una rappresentazione testuale della colonna.
         *
         * @return una stringa contenente nome e tipo della colonna
         */
        @Override
        public String toString() {
            return name + " + " + type;
        }
    }

    /** Lista delle colonne che definiscono lo schema. */
    private final List<Column> tableSchema = new ArrayList<>();

    /**
     * Costruisce lo schema della tabella interrogando i metadati del database tramite JDBC.
     *
     * @param db        l’accesso al database già connesso
     * @param tableName il nome della tabella della quale estrarre lo schema
     * @throws SQLException se si verificano errori durante l’accesso ai metadati
     */
    public TableSchema(DBAccess db, String tableName) throws SQLException {

        final HashMap<String, String> typeMap = new HashMap<>();
        typeMap.put("BIT", "string");
        typeMap.put("INT", "number");
        typeMap.put("CHAR", "string");
        typeMap.put("LONG", "number");
        typeMap.put("SHORT", "number");
        typeMap.put("FLOAT", "number");
        typeMap.put("DOUBLE", "number");
        typeMap.put("VARCHAR", "string");
        typeMap.put("LONGVARCHAR", "string");

        Connection conn = db.getConnection();
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet results = meta.getColumns(null, null, tableName, null);

        while (results.next()) {
            String sqlType = results.getString("TYPE_NAME");
            if (typeMap.containsKey(sqlType)) {
                tableSchema.add(new Column(
                    results.getString("COLUMN_NAME"),
                    typeMap.get(sqlType)
                ));
            }
        }

        results.close();
    }

    /**
     * Restituisce il numero di colonne che compongono lo schema della tabella.
     *
     * @return il numero di colonne nello schema
     */
    public int getNumberOfAttributes() {
        return tableSchema.size();
    }

    /**
     * Restituisce la colonna alla posizione indicata.
     *
     * @param index l’indice della colonna richiesta
     * @return la colonna corrispondente
     * @throws IndexOutOfBoundsException se {@code index} non è valido
     */
    public Column getColumn(int index) {
        return tableSchema.get(index);
    }
}