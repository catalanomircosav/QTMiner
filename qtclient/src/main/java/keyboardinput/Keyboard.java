package keyboardinput;

import java.io.*;
import java.util.*;

/**
 * Classe di utilità per la lettura da tastiera (standard input).
 * <p>
 * Fornisce metodi statici per leggere parole, stringhe, numeri, caratteri e booleani,
 * gestendo gli errori in modo centralizzato attraverso il contatore {@code errorCount}.
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

    /** Costruttore privato: classe utility, non istanziabile. */
    private Keyboard() { }

    /** @return numero di errori finora riscontrati */
    public static int getErrorCount() {
        return errorCount;
    }

    /** Reimposta il contatore degli errori a 0. */
    public static void resetErrorCount(int ignored) {
        errorCount = 0;
    }

    /** @return {@code true} se la stampa errori è attiva */
    public static boolean getPrintErrors() {
        return printErrors;
    }

    /** Abilita o disabilita la stampa errori. */
    public static void setPrintErrors(boolean flag) {
        printErrors = flag;
    }

    /** Stampa un errore e incrementa il contatore se consentito. */
    private static void error(String msg) {
        errorCount++;
        if (printErrors) System.err.println(msg);
    }

    /** Restituisce il prossimo token. */
    private static String getNextToken() {
        return getNextToken(true);
    }

    /** Restituisce il prossimo token (eventualmente saltando delimitatori). */
    private static String getNextToken(boolean skip) {
        if (current_token == null)
            return getNextInputToken(skip);
        String token = current_token;
        current_token = null;
        return token;
    }

    /** Legge un token dalla sorgente di input. */
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

    /** @return true se non ci sono più token nella riga corrente */
    public static boolean endOfLine() {
        return reader == null || !reader.hasMoreTokens();
    }

    // ------------------- METODI DI LETTURA PUBBLICI -------------------

    /** Legge una stringa completa. */
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

    /** Legge una singola parola. */
    public static String readWord() {
        try {
            return getNextToken();
        } catch (Exception e) {
            error("Errore nella lettura di una parola: restituito null.");
            return null;
        }
    }

    /** Legge un booleano (accetta \"true\"/\"false\", case-insensitive). */
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

    /** Legge un carattere. */
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

    /** Legge un intero. */
    public static int readInt() {
        try {
            return Integer.parseInt(getNextToken());
        } catch (Exception e) {
            error("Errore nella lettura di un intero: restituito MIN_VALUE.");
            return Integer.MIN_VALUE;
        }
    }

    /** Legge un long. */
    public static long readLong() {
        try {
            return Long.parseLong(getNextToken());
        } catch (Exception e) {
            error("Errore nella lettura di un long: restituito MIN_VALUE.");
            return Long.MIN_VALUE;
        }
    }

    /** Legge un float. */
    public static float readFloat() {
        try {
            return Float.parseFloat(getNextToken());
        } catch (Exception e) {
            error("Errore nella lettura di un float: restituito NaN.");
            return Float.NaN;
        }
    }

    /** Legge un double. */
    public static double readDouble() {
        try {
            return Double.parseDouble(getNextToken());
        } catch (Exception e) {
            error("Errore nella lettura di un double: restituito NaN.");
            return Double.NaN;
        }
    }
}