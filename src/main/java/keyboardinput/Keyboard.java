package keyboardinput;

import java.io.*;
import java.util.*;

/**
* Classe per la lettura di input da tastiera (standard input).
* <p>
* Fornisce metodi statici per leggere stringhe, numeri, caratteri e booleani.
* Gestisce inoltre eventuali errori di input, contando e mostrando i messaggi
* d’errore.
* </p>
*
* @author Mirco Catalano
* @author Lorenzo Amato
*/
public class Keyboard {

    /**Flag che abilita o disabilita la stampa degli errori. */
	private static boolean printErrors = true;

	/** Contatore degli errori riscontrati. */
	private static int errorCount = 0;

	/** Token corrente già letto ma non ancora utilizzato. */
	private static String current_token = null;

	/** Tokenizer per l’analisi dei dati letti. */
	private static StringTokenizer reader;

	/**
	 * Costruttore di default.
	 * Crea un'istanza della classe Keyboard.
	 */
	public Keyboard() { }

	/** Buffer di input collegato allo standard input. */
	private static final BufferedReader in = 
            new BufferedReader(new InputStreamReader(System.in));

	/**
	 * Restituisce il numero di errori riscontrati finora.
	 *
	 * @return numero di errori
	 */
	public static int getErrorCount()
	{
		return errorCount;
	}

	/**
	 * Reimposta il contatore degli errori a 0.
	 *
	 * @param count parametro ignorato (compatibilità con versioni precedenti)
	 */
	public static void resetErrorCount(int count)
	{
		errorCount = 0;
	}

	/**
	 * Restituisce se la stampa degli errori è attiva.
	 *
	 * @return true se la stampa è attiva, false altrimenti
	 */
	public static boolean getPrintErrors()
	{
		return printErrors;
	}

	/**
	 * Imposta se abilitare o disabilitare la stampa degli errori.
	 *
	 * @param flag true per abilitare la stampa, false per disabilitarla
	 */
	public static void setPrintErrors(boolean flag)
	{
		printErrors = flag;
	}

	/**
	 * Incrementa il contatore degli errori e stampa il messaggio
	 * se {@link #printErrors} è abilitato.
	 *
	 * @param str messaggio di errore
	 */
	private static void error(String str)
	{
		errorCount++;
		if (printErrors) System.out.println(str);
	}

	/**
	 * Restituisce il prossimo token, leggendo da input se necessario.
	 *
	 * @return il prossimo token
	 */
	private static String getNextToken()
	{
		return getNextToken(true);
	}

	/**
	 * Restituisce il prossimo token, con la possibilità di saltare i delimitatori.
	 *
	 * @param skip true se i delimitatori vanno ignorati
	 * @return token letto
	 */
	private static String getNextToken(boolean skip)
	{
		String token;
		if (current_token == null)
			token = getNextInputToken(skip);
		else
		{
			token = current_token;
			current_token = null;
		}

		return token;
	}

	/**
	 * Legge il prossimo token dall’input (anche da linee successive).
	 *
	 * @param skip true per ignorare delimitatori (spazi, tab, ecc.)
	 * @return token letto o null in caso di errore
	 */
	private static String getNextInputToken(boolean skip)
	{
		final String delimiters = " \t\n\r\f";
		String token = null;

		try
		{
			if (reader == null)
				reader = new StringTokenizer(in.readLine(), delimiters, true);

			while (token == null || ((delimiters.indexOf(token) >= 0) && skip)) {
				while (!reader.hasMoreTokens())
					reader = new StringTokenizer(in.readLine(), delimiters, true);
				token = reader.nextToken();
			}
		} 
		catch (Exception exception)
		{
			token = null;
		}

		return token;
	}

	/**
	 * Indica se non ci sono più token da leggere nella linea corrente.
	 *
	 * @return true se la linea è terminata, false altrimenti
	 */
	public static boolean endOfLine()
	{
		return !reader.hasMoreTokens();
	}

	/**
	 * Legge una stringa dallo standard input.
	 *
	 * @return stringa letta, oppure null in caso di errore
	 */
	public static String readString()
	{
		String str;
		try
		{
			str = getNextToken(false);
			while (!endOfLine())
				str = str + getNextToken(false);
		} 
		catch (Exception exception)
		{
			error("Errore nella lettura di una stringa: restituito null.");
			str = null;
		}

		return str;
	}

	/**
	 * Legge una parola (token) dallo standard input.
	 *
	 * @return parola letta, oppure null in caso di errore
	 */
	public static String readWord()
	{
		String token;
		try
		{
			token = getNextToken();
		} catch (Exception exception)
		{
			error("Errore nella lettura di una parola: restituito null.");
			token = null;
		}
		return token;
	}

	/**
	 * Legge un valore booleano dallo standard input.
	 * Accetta "true" o "false" (case insensitive).
	 *
	 * @return valore booleano letto, oppure false in caso di errore
	 */
	public static boolean readBoolean()
	{
		String token = getNextToken();
		boolean bool;
		try
		{
			if (token.toLowerCase().equals("true"))
				bool = true;
			else if (token.toLowerCase().equals("false"))
				bool = false;
			else
			{
				error("Errore nella lettura di un booleano: restituito false.");
				bool = false;
			}
		} 
		catch (Exception exception)
		{
			error("Errore nella lettura di un booleano: restituito false.");
			bool = false;
		}
		return bool;
	}

	/**
	 * Legge un carattere dallo standard input.
	 *
	 * @return carattere letto, oppure {@link Character#MIN_VALUE} in caso di errore
	 */
	public static char readChar()
	{
		String token = getNextToken(false);
		char value;
		try
		{
			if (token.length() > 1)
				current_token = token.substring(1);
			else
				current_token = null;
			value = token.charAt(0);
		} 
		catch (Exception exception)
		{
			error("Errore nella lettura di un carattere: restituito MIN_VALUE.");
			value = Character.MIN_VALUE;
		}
		return value;
	}

	/**
	 * Legge un intero dallo standard input.
	 *
	 * @return intero letto, oppure {@link Integer#MIN_VALUE} in caso di errore
	 */
	public static int readInt() {
		String token = getNextToken();
		int value;
		try
		{
			value = Integer.parseInt(token);
		} 
		catch (Exception exception)
		{
			error("Errore nella lettura di un intero: restituito MIN_VALUE.");
			value = Integer.MIN_VALUE;
		}
		return value;
	}

	/**
	 * Legge un long dallo standard input.
	 *
	 * @return long letto, oppure {@link Long#MIN_VALUE} in caso di errore
	 */
	public static long readLong() {
		String token = getNextToken();
		long value;
		try
		{
			value = Long.parseLong(token);
		} 
		catch (Exception exception)
		{
			error("Errore nella lettura di un long: restituito MIN_VALUE.");
			value = Long.MIN_VALUE;
		}

		return value;
	}

	/**
	 * Legge un float dallo standard input.
	 *
	 * @return float letto, oppure {@link Float#NaN} in caso di errore
	 */
	public static float readFloat()
	{
		String token = getNextToken();
		float value;
		try
		{
			value = Float.parseFloat(token);
		} 
		catch (Exception exception)
		{
			error("Errore nella lettura di un float: restituito NaN.");
			value = Float.NaN;
		}

		return value;
	}

	/**
	 * Legge un double dallo standard input.
	 *
	 * @return double letto, oppure {@link Double#NaN} in caso di errore
	 */
	public static double readDouble()
	{
		String token = getNextToken();
		double value;
		try
		{
			value = Double.parseDouble(token);
		}
		catch (Exception exception)
		{
			System.err.println("Errore nella lettura di un double.");
			value = Double.NaN;
		}
		
		return value;
	}
}