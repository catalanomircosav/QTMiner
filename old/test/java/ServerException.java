package test.java;

/**
 * Eccezione lanciata in caso di errore restituito dal server.
 * <p>
 * Questa eccezione viene sollevata dal client quando il server segnala
 * una condizione di errore durante l'esecuzione di una richiesta.
 * </p>
 */
public class ServerException extends Exception {

    /**
     * Costruisce una {@code ServerException} con il messaggio specificato.
     *
     * @param message il messaggio descrittivo dell'errore generato dal server
     */
    public ServerException(String message) {
        super(message);
    }

    /**
     * Costruisce una {@code ServerException} senza un messaggio descrittivo.
     */
    public ServerException() {
        super();
    }
}