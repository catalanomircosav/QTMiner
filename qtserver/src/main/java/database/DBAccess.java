package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import exceptions.DatabaseConnectionException;

/**
 * Classe che gestisce la connessione a un database MySQL.
 * <p>
 * Questa classe fornisce metodi per stabilire, ottenere e chiudere la connessione
 * a un database MySQL utilizzando i parametri di configurazione predefiniti.
 * </p>
 * 
 * @author Mirco Catalano
 * @author Lorenzo Amato
 */
public class DBAccess
{
    /**
     * Porta su cui il server MySQL e' in ascolto
     */
    private final int PORT = 3306;

    /**
     * Password per l'autenticazione al database
     */
    private final String PASSWORD = "map";

    /**
     * Nome del database  a cui connettersi
     */
    private final String DATABASE = "MapDB";

    /**
     * Nome utente per l'autenticazione al database
     */
    private final String USER_ID = "MapUser";

    /**
     * Protocollo del DBMS
     */
    private final String DBMS = "jdbc:mysql";

    /**
     * Indirizzo del server database
     */
    private final String SERVER = "localhost";

    /**
     * Nome della calsse del driver JDBC per MYSQL
     */
    private final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";

    /**
     * Connessione attiva al database
     */
    private Connection conn;

    /**
     * Costruisce un nuovo oggetto {@code DBAccess}
     * <p>
     * Il costruttore non inizializza immediatamente la connessione.
     * Per stabilire la connessione è necessario chiamare {@link #initConnection()}.
     * </p>
     */
    public DBAccess() { }

    /**
     * Inizializza la connessione al database.
     * 
     * <p>
     * Carica il driver JDBC, costruisce la stringa di connessione e tenta
     * di stabilire una connessione con il database utilizzando le credenziali
     * configurate.
     * </p>
     * 
     * @throws SQLException se si verifica un errore durante l'accesso al database
     * @throws DatabaseConnectionException se il driver JDBC non è trovato o 
     *         la connessione non può essere stabilita
     */
    public void initConnection() throws SQLException, DatabaseConnectionException
    {
        try
        {
            Class.forName(DRIVER_CLASS_NAME);
        } catch(ClassNotFoundException exception)
        {
            throw new DatabaseConnectionException("Driver MySQL non trovato: " + exception.getMessage());
        }
        StringBuilder sb = new StringBuilder();
        
        sb.append(DBMS).append("://").append(SERVER).append(":")
            .append(PORT).append("/").append(DATABASE).append("?user=")
            .append(USER_ID).append("&password=").append(PASSWORD).append("&serverTimezone=UTC");

        /* ? debug */ // System.out.println("Connection's String: " + sb.toString());

        try
        {
            conn = DriverManager.getConnection(sb.toString());
        } catch(SQLException exception)
        {
            System.err.println(exception.getMessage());

            throw new DatabaseConnectionException();
        }
    }

    /**
     * Restituisce la connessione attiva al database.
     * 
     * @return l'oggetto {@link Connection} rappresentante la connessione attiva
     */
    public Connection getConnection()
    {
        return conn;
    }

    /**
     * Chiude la connessione attiva al database.
     * 
     * @throws SQLException se si verifica un errore durante la chiusura della connessi
     */
    public void closeConnection() throws SQLException
    {
        conn.close();
    }
}