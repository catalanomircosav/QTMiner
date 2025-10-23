package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


import exceptions.EmptySetException;
import exceptions.NoValueException;

import database.TableSchema.Column;

/**
 * Classe che rappresenta i dati di una tabella del database.
 * <p>
 * Questa classe consente di accedere e manipolare i dati memorizzati in una tabella
 * del database attraverso operazioni di lettura, estrazione di valori distinti e calcolo
 * di valori aggregati (minimo e massimo). 
 * Ogni riga della tabella viene modellata da un oggetto {@link Example}.
 * </p>
 * 
 * @author Mirco Catalano
 * @author Lorenzo Amato
 */
public class TableData 
{

	DBAccess db;
	
	/**
	 * Costruisce un oggetto TableData associato al database specificato.
	 * 
	 * @param db accesso al database
	 */
	public TableData(DBAccess db) 
	{
		this.db = db;
	}

	/**
	 * Estrae le transazioni distinte presenti nella tabella specificata.
	 * <p>
	 * Ogni transazione è rappresentata da un'istanza della classe {@link Example},
	 * i cui valori corrispondono ai campi della tupla. 
	 * Il metodo genera un'eccezione se il result set risultante è vuoto.
	 * </p>
	 * 
	 * @param table nome della tabella nel database
	 * 
	 * @return lista contenente le transazioni distinte presenti nella tabella
	 * 
	 * @throws SQLException se si verifica un errore durante l'esecuzione della query
	 * @throws EmptySetException se il result set è vuoto
	 */
	public List<Example> getDistinctTransactions(String table) throws SQLException, EmptySetException
	{
		LinkedList<Example> transSet = new LinkedList<Example>();
		Statement statement;
		TableSchema tSchema=new TableSchema(db,table);
		
		String query="select distinct ";
		
		for(int i=0;i<tSchema.getNumberOfAttributes();i++)
		{
			Column c=tSchema.getColumn(i);
			if(i>0)
				query+=",";
			query += c.getColumnName();
		}

		if(tSchema.getNumberOfAttributes()==0)
			throw new SQLException();
		query += (" FROM "+table);
		
		statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);
		boolean empty=true;

		while (rs.next()) 
		{
			empty=false;
			Example currentTuple=new Example();
			for(int i=0;i<tSchema.getNumberOfAttributes();i++)
				if(tSchema.getColumn(i).isNumber())
					currentTuple.add(rs.getDouble(i+1));
				else
					currentTuple.add(rs.getString(i+1));
			transSet.add(currentTuple);
		}

		rs.close();
		statement.close();

		if(empty) throw new EmptySetException();
		
		return transSet;
	}

	/**
	 * Estrae i valori distinti e ordinati in modo crescente della colonna specificata.
	 * <p>
	 * I valori distinti vengono letti dal database e inseriti in un insieme ordinato
	 * (implementato tramite {@link TreeSet}), che viene restituito al chiamante.
	 * </p>
	 * 
	 * @param table nome della tabella nel database
	 * @param column colonna di cui estrarre i valori distinti
	 * 
	 * @return insieme di valori distinti e ordinati della colonna specificata
	 * 
	 * @throws SQLException se si verifica un errore durante l'esecuzione della query
	 */
	public  Set<Object>getDistinctColumnValues(String table,Column column) throws SQLException
	{
		Set<Object> valueSet = new TreeSet<Object>();
		Statement statement;
		TableSchema tSchema=new TableSchema(db,table);
		
		String query="select distinct ";
		
		query+= column.getColumnName();
		
		query += (" FROM "+table);
		
		query += (" ORDER BY " +column.getColumnName());
		
		statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);

		while (rs.next()) 
		{
				if(column.isNumber())
					valueSet.add(rs.getDouble(1));
				else
					valueSet.add(rs.getString(1));
		}

		rs.close();
		statement.close();
		
		return valueSet;
	}

	/**
	 * Calcola un valore aggregato (minimo o massimo) per la colonna specificata.
	 * <p>
	 * Il tipo di aggregazione è determinato dal parametro {@code aggregate},
	 * che può assumere i valori {@code QUERY_TYPE.MIN} o {@code QUERY_TYPE.MAX}.
	 * Se la tabella non contiene valori validi per la colonna, viene sollevata
	 * un'eccezione {@link NoValueException}.
	 * </p>
	 * 
	 * @param table nome della tabella nel database
	 * @param column colonna di cui calcolare il valore aggregato
	 * @param aggregate tipo di aggregazione da applicare (MIN o MAX)
	 * 
	 * @return valore minimo o massimo della colonna specificata
	 * 
	 * @throws SQLException se si verifica un errore durante l'esecuzione della query
	 * @throws NoValueException se il result set è vuoto o il valore è nullo
	 */
	public  Object getAggregateColumnValue(String table,Column column,QUERY_TYPE aggregate) throws SQLException,NoValueException
	{
		Statement statement;
		TableSchema tSchema=new TableSchema(db,table);
		Object value=null;
		String aggregateOp="";
		
		String query="select ";
		if(aggregate==QUERY_TYPE.MAX)
			aggregateOp+="max";
		else
			aggregateOp+="min";
		query+=aggregateOp+"("+column.getColumnName()+ ") FROM "+table;
		
		
		statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);
		if (rs.next()) 
		{
				if(column.isNumber())
					value=rs.getFloat(1);
				else
					value=rs.getString(1);
		}

		rs.close();
		statement.close();

		if(value==null)
			throw new NoValueException("No " + aggregateOp+ " on "+ column.getColumnName());
			
		return value;
	}
}
