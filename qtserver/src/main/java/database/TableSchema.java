package database;

import java.util.List;
import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.DatabaseMetaData;

/**
 * Classe che rappresenta lo schema di una tabella di un database.
 * <p>
 * Questa classe estrae e memorizza i metadati di una tabella del database.
 * </p>
 * 
 * @author Mirco Catalano
 * @author Lorenzo Amato
 */
public class TableSchema
{
    /**
     * Classe che rappresenta uno schema della tabella
     */
    public class Column
    {
        /**
         * Nome della colonna
         */
        private String name;

        /**
         * Tipo di dato semplificato
         */
        private String type;

        /**
         * Costruisce una nuova colonna con nome e tipo specificati
         * 
         * @param name nome della colonne
         * @param type tipo della colonna
         */
        Column(String name, String type)
        {
            this.name = name;
            this.type = type;
        }

        /**
         * Restituisce il nome della colonna
         * 
         * @return nome della colonna
         */
        public String getColumnName()
        {
            return name;
        }

        /**
         * Verifica se la colonna contiene valori numerici
         * 
         * @return {@code true} se il tipo della colonna e' un numero
         *         {@code false} se e' una stringa
         */
        public boolean isNumber()
        {
            return type.equals("number");
        }

        /**
         * Restituisce una rappresentazione testuale della colonna
         * 
         * @return stringa contenente nome e tipo della colonna, separati da " + ".
         */
        public String toString()
        {
            StringBuilder sb = new StringBuilder();

            sb.append(name).append(" + ").append(type);
            
            return sb.toString();   
        }
    }

    /**
     * Riferimento all'accesso al database
     */
    private DBAccess db;

    /**
     * Lista delle colonne che compongono lo schema
     */
    private List<Column> tableSchema = new ArrayList<Column>();

    /**
     * Costruisce lo schema della tabella specificata.
     * <p>
     * Estrae i metadati della tabella dal database e mappa i tipi SQL nativi
     * ai tipi JAVA semplificati
     * </p>
     * 
     * @param db accesso al database
     * @param tableName nome della tabella di cui estrarre lo schema
     * 
     * @throws SQLException se si verifica un errore durante l'accesso ai dati
     */
    public TableSchema(DBAccess db, String tableName) throws SQLException
    {
        this.db = db;

        HashMap<String, String> mapSQL_JAVATypes = new HashMap<String, String>();
        
        mapSQL_JAVATypes.put("BIT", "string");
        mapSQL_JAVATypes.put("INT", "number");
        mapSQL_JAVATypes.put("CHAR", "string");
        mapSQL_JAVATypes.put("LONG", "number");
        mapSQL_JAVATypes.put("SHORT", "number");
        mapSQL_JAVATypes.put("FLOAT", "number");
        mapSQL_JAVATypes.put("DOUBLE", "number");
        mapSQL_JAVATypes.put("VARCHAR", "string");
        mapSQL_JAVATypes.put("LONGVARCHAR", "string");
        
        Connection conn = db.getConnection();
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet results = meta.getColumns(null, null, tableName, null);
    
        while(results.next())
        {
            if(mapSQL_JAVATypes.containsKey(results.getString("TYPE_NAME")))
                tableSchema.add(new Column(
                    results.getString("COLUMN_NAME"),
                    mapSQL_JAVATypes.get(results.getString("TYPE_NAME"))
                ));
        }

        results.close();
    }

    /**
     * Restituisce il numero di colonne dello schema della tabella
     * 
     * @return il numero di colonne nella tabella
     */
    public int getNumberOfAttributes()
    {
        return tableSchema.size();
    }

    /**
     * Restituisce la colonna alla posizione specificata
     * 
     * @param index indice della colonna
     *
     * @return l'oggetto alla posizione sepcificata
     */
    public Column getColumn(int index)
    {
        return tableSchema.get(index);
    }
}
