package utility;

import java.util.Arrays;

/**
 * la classe {@code ArraySet} modella il dato astratto insieme di interi e ne fornisce una realizzazione basata su vettore di booleani.
 * 
 * @author Mirco Catalano
 * @author Lorenzo Amato
 * 
 * @version 1.1
 * @since 2.0
 */
public class ArraySet
{
	private boolean[] set;
	
	private int size;
	private int cardinality;
	
	/**
	 * Costruisce un nuovo insieme vuoto, con capacità iniziale di 50 elementi.
	 */
	public ArraySet()
	{
		size = 0;
		cardinality = 0;

		set = new boolean[50];
		Arrays.fill(set, false);
	}
	
	/**
	 * Aggiunge l'intero specificato all'insieme.
	 * Se l'elemento è già presente, l'insieme rimane invariato;
	 * altrimenti, l'elemento viene aggiunto e la cardinalità dell'insieme aumenta di uno.
	 * @param i l'intero da aggiungere all'insieme
	 * @return {@code true} se l'elemento è stato aggiunto (non era già presente), {@code false} altrimenti
	 */
	public boolean add(int i)
	{
		if(i >= set.length)
		{
			boolean[] temp = new boolean[set.length * 2];
			Arrays.fill(temp,false);

			System.arraycopy(set, 0, temp, 0, set.length);
			set = temp;
		}

		boolean added = set[i];
		set[i] = true;
		
		if(i >= size) size = i + 1;
		if(!added) cardinality++;
		
		return !added;
	}
	
	/**
	 * Rimuove l'intero specificato dall'insieme.
	 * Se l'elemento è presente, viene rimosso e la cardinalità dell'insieme diminuisce di uno;
	 * altrimenti, l'insieme rimane invariato.
	 * @param i l'intero da rimuovere dall'insieme
	 * @return {@code true} se l'elemento è stato rimosso (era presente), {@code false} altrimenti
	 */
	public boolean delete(int i)
	{
		if(i < size)
		{
			boolean deleted = set[i];
			set[i] = false;

			if(i == size - 1)
			{
				int j;
				for(j = size - 1; j >= 0 && !set[j]; j--);
				
				size = j + 1;
			}

			if(deleted) cardinality--;
			
			return deleted;
		}

		return false;
	}
	
	/**
	 * Verifica se l'insieme contiene l'intero specificato.
	 * @param i l'intero da verificare
	 * @return {@code true} se l'elemento è presente nell'insieme, {@code false} altrimenti
	 */
	public boolean get(int i) { return set[i]; }

	/**
	 * Restituisce la capacità attuale dell'insieme, ovvero il numero massimo di elementi che può contenere senza dover ridimensionare l'array sottostante.
	 * 
	 * @return la capacità attuale dell'insieme
	 */
	public int size() { return cardinality; }
	
	/**
	 * Restituisce la cardinalità dell'insieme, ovvero il numero di elementi attualmente presenti nell'insieme.
	 * 
	 * @return la cardinalità dell'insieme
	 */
	public int[] toArray()
	{
		int a[] = new int[0];
		for(int i = 0;i < size; i++)
		{
			if(get(i))
			{
				int[] temp = new int[a.length + 1];
				System.arraycopy(a, 0, temp, 0, a.length);

				a = temp;
				a[a.length - 1] = i;
			}
		}
		return a;
	}
}
