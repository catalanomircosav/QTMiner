package keyboardinput;

import java.io.*;
import java.util.*;

/**
 * Classe di utilità per la lettura da tastiera tramite lo standard input.
 * <p>
 * Fornisce metodi statici per leggere parole, stringhe, numeri, caratteri e
 * valori booleani. In caso di errori di parsing o formattazione, il problema
 * viene gestito centralmente incrementando il contatore {@code errorCount}.
 * </p>
 */
public class Keyboard {

    /** Flag che abilita o disabilita la stampa degli errori. */
    private static boolean printErrors = true;

    /** Contatore degli errori riscontrati. */
    private static int errorCount = 0;

    /** Token corrente già letto ma non ancora consumato. */
    private static String current_token = null;

    /** Tokenizer dell’input. */
    private static StringTokenizer reader;

    /** Buffer di input collegato allo standard input. */
    private static final BufferedReader in =
            new BufferedReader(new InputStreamReader(System.in));

    /**
     * Costruttore privato per impedire l'istanza della classe di utilità.
     */
    private Keyboard() { }

    /**
     * Restituisce il numero totale di errori riscontrati nelle letture effettuate.
     *
     * @return il numero di errori riscontrati
     */
    public static int getErrorCount() {
        return errorCount;
    }

    /**
     * Reimposta il contatore degli errori a zero.
     *
     * @param ignored parametro ignorato, presente per compatibilità
     */
    public static void resetErrorCount(int ignored) {
        errorCount = 0;
    }

    /**
     * Verifica se la stampa degli errori è attualmente abilitata.
     *
     * @return {@code true} se la stampa degli errori è attiva; {@code false} altrimenti
     */
    public static boolean getPrintErrors() {
        return printErrors;
    }

    /**
     * Abilita o disabilita la stampa degli errori su {@code System.err}.
     *
     * @param flag {@code true} per abilitare la stampa; {@code false} per disabilitarla
     */
    public static void setPrintErrors(boolean flag) {
        printErrors = flag;
    }

    /**
     * Stampa un messaggio di errore e incrementa il contatore degli errori se la stampa è abilitata.
     *
     * @param msg il messaggio di errore da visualizzare
     */
    private static void error(String msg) {
        errorCount++;
        if (printErrors) System.err.println(msg);
    }

    /**
     * Restituisce il prossimo token disponibile dalla sorgente di input.
     *
     * @return il prossimo token disponibile
     */
    private static String getNextToken() {
        return getNextToken(true);
    }

    /**
     * Restituisce il prossimo token, eventualmente saltando i delimitatori.
     *
     * @param skip {@code true} per saltare delimitatori e spazi; {@code false} per leggerli
     * @return il token letto oppure {@code null} in caso di errore
     */
    private static String getNextToken(boolean skip) {
        if (current_token == null)
            return getNextInputToken(skip);
        String token = current_token;
        current_token = null;
        return token;
    }

    /**
     * Legge un token dalla sorgente di input, eventualmente saltando i delimitatori.
     *
     * @param skip {@code true} per ignorare i delimitatori; {@code false} per considerarli
     * @return il token letto oppure {@code null} in caso di errore
     */
    private static String getNextInputToken(boolean skip) {
        final String delimiters = " \t\n\r\f";
        String token = null;

        try {
            if (reader == null)
                reader = new StringTokenizer(in.readLine(), delimiters, true);

            while (token == null || ((delimiters.indexOf(token) >= 0) && skip)) {
                while (!reader.hasMoreTokens())
                    reader = new StringTokenizer(in.readLine(), delimiters, true);
                token = reader.nextToken();
            }
        } catch (Exception e) {
            token = null;
        }

        return token;
    }

    /**
     * Verifica se non sono presenti ulteriori token nella riga corrente.
     *
     * @return {@code true} se non vi sono altri token disponibili; {@code false} altrimenti
     */
    public static boolean endOfLine() {
        return reader == null || !reader.hasMoreTokens();
    }

    /**
     * Legge una stringa completa fino al termine della riga.
     *
     * @return la stringa letta, oppure {@code null} in caso di errore
     */
    public static String readString() {
        try {
            String str = getNextToken(false);
            while (!endOfLine())
                str += getNextToken(false);
            return str;
        } catch (Exception e) {
            error("Errore nella lettura di una stringa: restituito null.");
            return null;
        }
    }

    /**
     * Legge una singola parola dalla linea di input.
     *
     * @return la parola letta, oppure {@code null} in caso di errore
     */
    public static String readWord() {
        try {
            return getNextToken();
        } catch (Exception e) {
            error("Errore nella lettura di una parola: restituito null.");
            return null;
        }
    }

    /**
     * Legge un valore booleano dalla tastiera. Sono accettati i valori
     * {@code "true"} e {@code "false"} (maiuscolo o minuscolo).
     *
     * @return il valore booleano letto, oppure {@code false} in caso di errore
     */
    public static boolean readBoolean() {
        String token = getNextToken();
        try {
            if ("true".equalsIgnoreCase(token))
                return true;
            if ("false".equalsIgnoreCase(token))
                return false;
            error("Booleano non valido: restituito false.");
        } catch (Exception e) {
            error("Errore nella lettura di un booleano: restituito false.");
        }
        return false;
    }

    /**
     * Legge un carattere dalla tastiera.
     *
     * @return il carattere letto, oppure {@code Character.MIN_VALUE} in caso di errore
     */
    public static char readChar() {
        String token = getNextToken(false);
        try {
            if (token.length() > 1)
                current_token = token.substring(1);
            return token.charAt(0);
        } catch (Exception e) {
            error("Errore nella lettura di un carattere: restituito MIN_VALUE.");
            return Character.MIN_VALUE;
        }
    }

    /**
     * Legge un valore intero dalla tastiera.
     *
     * @return l'intero letto, oppure {@code Integer.MIN_VALUE} in caso di errore
     */
    public static int readInt() {
        try {
            return Integer.parseInt(getNextToken());
        } catch (Exception e) {
            error("Errore nella lettura di un intero: restituito MIN_VALUE.");
            return Integer.MIN_VALUE;
        }
    }

    /**
     * Legge un valore long dalla tastiera.
     *
     * @return il valore long letto, oppure {@code Long.MIN_VALUE} in caso di errore
     */
    public static long readLong() {
        try {
            return Long.parseLong(getNextToken());
        } catch (Exception e) {
            error("Errore nella lettura di un long: restituito MIN_VALUE.");
            return Long.MIN_VALUE;
        }
    }

    /**
     * Legge un valore float dalla sorgente di input.
     *
     * @return il valore float letto, oppure {@code Float.NaN} in caso di errore
     */
    public static float readFloat() {
        try {
            return Float.parseFloat(getNextToken());
        } catch (Exception e) {
            error("Errore nella lettura di un float: restituito NaN.");
            return Float.NaN;
        }
    }

    /**
     * Legge un valore double dalla sorgente di input.
     *
     * @return il valore double letto, oppure {@code Double.NaN} in caso di errore
     */
    public static double readDouble() {
        try {
            return Double.parseDouble(getNextToken());
        } catch (Exception e) {
            error("Errore nella lettura di un double: restituito NaN.");
            return Double.NaN;
        }
    }
}