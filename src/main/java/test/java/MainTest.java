package test.java;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import data.Data;
import exceptions.ClusteringRadiusException;
import exceptions.EmptyDatasetException;
import exceptions.DatabaseConnectionException;


import keyboardinput.Keyboard;

import mining.QTMiner;

/**
 * Classe di test per l'algoritmo di clustering {@code QTMiner}.
 * 
 * @see Data
 * @see QTMiner
 * 
 * @author Mirco Catalano
 * @author Lorenzo Amato
 */
public class MainTest {

	/**
	 * Costruttore di default.
	 * Crea un'istanza della classe MainTest.
	 */
	public MainTest() { }

	/**
	 * Avvia l'esecuzione del programma.
	 * @param args command line arguments (not used)
	 */
	public static void main(String[] args) 
	{
		QTMiner qt;
		Data data = null;
		String file;

		boolean loaded;

		try
		{
			do
			{
				loaded = false;
				while(!loaded)
				{
					System.out.print("Nome tabella: ");
					String tableName = Keyboard.readString();
					try
					{
						data = new Data(tableName);
						loaded = true;
					}
					catch (EmptyDatasetException e) {
						System.err.println("La tabella '" + tableName + "' è vuota. Riprova.");
						loaded = false;
					} 
					catch (DatabaseConnectionException e) {
						System.err.println("Errore di connessione al database: " + e.getMessage());
						loaded = false;
					} 
					catch (SQLException e) {
						System.err.println("Tabella inesistente.");
						loaded = false;
					}
				};

				if(loaded)
				{
					System.out.println("Scegli un'opzione");
					System.out.println("(1) Carica Cluster da File.");
					System.out.println("(2) Carica Dati.");
					System.out.print("Risposta: ");
					
					switch(Keyboard.readInt())
					{
						case 1:
							System.out.print("Nome backup: "); file = Keyboard.readString();
							file = (file != null && file.contains(".dmp")) ? file : file + ".dmp";

							qt = new QTMiner(file);

							System.out.println(qt.getC().toString(data));
						break;

						case 2:
							do
							{
								System.out.println(data);
								System.out.print("Inserisci raggio (>0): "); qt = new QTMiner(Keyboard.readDouble());

								System.out.println("\nNumber of clusters: " + qt.compute(data) + "\n");
								System.out.println(qt.getC().toString(data));

								System.out.print("File di backup: "); file = Keyboard.readString();
								file = (file != null && file.contains(".dmp")) ? file : file + ".dmp";
								
								qt.salva(file);
								
								System.out.print("Vuoi eseguire di nuovo? (s/n): ");
							} while(Keyboard.readString().equalsIgnoreCase("S"));
						break;
					}
				}

				System.out.print("Vuoi scegliere un'altra voce dal menu'? (s/n): ");
			} while(Keyboard.readString().equalsIgnoreCase("S"));
		}
		catch(ClusteringRadiusException e)
		{
			System.err.println(e.getMessage());
		}
		catch(FileNotFoundException e)
		{
			System.err.println(e.getMessage());
		}
		catch(IOException e)
		{
			System.err.println(e.getMessage());
		}
		catch(ClassNotFoundException e)
		{
			System.err.println(e.getMessage());
		}
	}
}