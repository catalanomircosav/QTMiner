package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import exceptions.DatabaseConnectionException;

/**
 * Gestisce la connessione a un database MySQL tramite JDBC.
 * <p>
 * Fornisce metodi per inizializzare, ottenere e chiudere una connessione
 * verso un DB MySQL basandosi sui parametri configurati internamente
 * (driver, host, porta e credenziali).
 * </p>
 *
 * @see Connection
 */
public class DBAccess {

    /** Porta su cui il server MySQL è in ascolto. */
    private static final int PORT = 3306;

    /** Password per l'autenticazione. */
    private static final String PASSWORD = "map";

    /** Nome del database. */
    private static final String DATABASE = "MapDB";

    /** Nome utente per l'autenticazione. */
    private static final String USER_ID = "MapUser";

    /** Protocollo JDBC. */
    private static final String DBMS = "jdbc:mysql";

    /** Host del server database. */
    private static final String SERVER = "localhost";

    /** Driver JDBC ufficiale per MySQL. */
    private static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";

    /** Connessione JDBC attiva. */
    private Connection conn;

    /**
     * Costruisce un oggetto {@code DBAccess} senza aprire la connessione.
     * Per stabilirla, invocare il metodo {@link #initConnection()}.
     */
    public DBAccess() { }

    /**
     * Inizializza la connessione al database MySQL utilizzando i parametri configurati.
     *
     * @throws SQLException se si verifica un errore lato JDBC durante la creazione della connessione
     * @throws DatabaseConnectionException se il driver non è disponibile o la connessione non può essere stabilita
     */
    public void initConnection() throws SQLException, DatabaseConnectionException {
        try {
            Class.forName(DRIVER_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            throw new DatabaseConnectionException("Driver MySQL non trovato: " + e.getMessage());
        }

        final String url = String.format(
            "%s://%s:%d/%s?user=%s&password=%s&serverTimezone=UTC",
            DBMS, SERVER, PORT, DATABASE, USER_ID, PASSWORD
        );

        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Impossibile connettersi al database: " + e.getMessage());
        }
    }

    /**
     * Restituisce la connessione JDBC attualmente attiva.
     *
     * @return la connessione, oppure {@code null} se non è stata inizializzata
     */
    public Connection getConnection() {
        return conn;
    }

    /**
     * Chiude la connessione al database, se attualmente aperta.
     *
     * @throws SQLException se si verifica un errore durante la chiusura
     */
    public void closeConnection() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}