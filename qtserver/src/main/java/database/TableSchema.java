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
 * dell'applicazione ("number" o "string").
 * </p>
 *
 * @see Column
 */
public class TableSchema {

    /**
     * Rappresenta una singola colonna dello schema della tabella.
     */
    public class Column {

        /** Nome della colonna. */
        private final String name;

        /** Tipo semplificato ("number" o "string"). */
        private final String type;

        /**
         * Costruisce una colonna dello schema.
         *
         * @param name nome della colonna
         * @param type tipo semplificato ("number" o "string")
         */
        Column(String name, String type) {
            this.name = name;
            this.type = type;
        }

        /**
         * @return il nome della colonna
         */
        public String getColumnName() {
            return name;
        }

        /**
         * @return {@code true} se la colonna contiene valori numerici
         */
        public boolean isNumber() {
            return type.equals("number");
        }

        /**
         * @return una rappresentazione testuale della colonna
         */
        @Override
        public String toString() {
            return name + " + " + type;
        }
    }

    /** Lista delle colonne che definiscono lo schema. */
    private final List<Column> tableSchema = new ArrayList<>();

    /**
     * Costruisce lo schema della tabella specificata interrogando il database.
     *
     * @param db accesso al database già connesso
     * @param tableName nome della tabella della quale estrarre lo schema
     *
     * @throws SQLException se si verificano errori durante l'accesso ai metadati
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
     * @return il numero di colonne nello schema
     */
    public int getNumberOfAttributes() {
        return tableSchema.size();
    }

    /**
     * Restituisce la colonna alla posizione indicata.
     *
     * @param index indice della colonna
     * @return la colonna corrispondente
     */
    public Column getColumn(int index) {
        return tableSchema.get(index);
    }
}