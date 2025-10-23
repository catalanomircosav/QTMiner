package data;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.LinkedList;

import database.Example;
import database.QUERY_TYPE;
import database.DBAccess;
import database.TableData;
import database.TableSchema;
import database.TableSchema.Column;

import java.net.ConnectException;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;

import exceptions.EmptySetException;
import exceptions.NoValueException;
import exceptions.EmptyDatasetException;
import exceptions.DatabaseConnectionException;


/**
 * Classe che rappresenta un insieme di esempi con attributi discreti,
 * strutturati in una matrice bidimensionale.
 * 
 * <p>
 * Ogni riga della matrice rappresenta un esempio, ogni colonna un attributo.
 * 
 * Questa implementazione contiene il dataset "PlayTennis" di esempio, con 14 esempi e 5 attributi discreti.
 * </p>
 * 
 * @author Mirco Catalano
 * @author Lorenzo Amato
 */
public class Data
{
    /**
     * Lista contenente gli esempi
     */
    private List<Example> data = new ArrayList<>();

    /**
     * Numero di esempi 
     */
    private int numberOfExamples = 14;

    /**
     * Schema degli attributi
     */
    private List<Attribute> attributeSet = new LinkedList<Attribute>();

    public Data(String tableName) throws DatabaseConnectionException, EmptyDatasetException, SQLException
    {
        DBAccess databaseAccess = new DBAccess();

        try
        {
            databaseAccess.initConnection();

            TableSchema tableSchema = new TableSchema(databaseAccess, tableName);

            createAttributesFromTableSchema(tableName, databaseAccess, tableSchema);

            TableData tableData = new TableData(databaseAccess);

            try
            {
                List<Example> transactions = tableData.getDistinctTransactions(tableName);
                
                if(transactions.isEmpty())
                    throw new EmptyDatasetException("La tabella " + tableName + " e' vuota.");
                
                data.addAll(transactions);
                numberOfExamples = data.size();
            }
            catch(EmptySetException exception)
            {
                System.err.println(exception.getMessage());
            }
        }
        catch(EmptyDatasetException exception)
        {
            throw exception;
        }
        catch(SQLException exception)
        {
            throw exception;
        }
        catch(DatabaseConnectionException exception)
        {
            throw exception;
        }
        finally 
        {
            try
            {
                databaseAccess.closeConnection();
            } 
            catch(SQLException exception)
            {
                System.err.println(exception.getMessage());
            }
        }
    }
    /**
     * Restituisce il numero totale di esempi presenti nel dataset
     * 
     * @return numero di esempi
     */
    public int getNumberOfExamples()
    {
        return numberOfExamples;
    }

    /**
     * Restituisce il numero totale di attributi del dataset
     * 
     * @return il numero di attributi
     */
    public int getNumberOfAttributes()
    {
        return attributeSet.size();
    }

    /**
     * Restituisce lo schema degli attributi del dataset
     * 
     * @return array contenente gli attributi
     */
    public Attribute[] getAttributeSchema()
    {
        return attributeSet.toArray(new Attribute[0]);
    }

    /**
     * Restituisce il valroe di un attributo per gli indici specificati
     * 
     * @param exampleIndex indice dell'esempio
     * @param attributeIndex indice dell'attributo
     * 
     * @return valore dell'attributo agli indici specificati
     * 
     * @throws ArrayIndexOutOfBoundsException se gli indici son ofuori dai limiti
     */
    public Object getValue(int exampleIndex, int attributeIndex)
    {
        if ((exampleIndex < 0 || exampleIndex > 14) 
            || ((attributeIndex < 0 || attributeIndex > 5)))
                throw new ArrayIndexOutOfBoundsException("Indici fuori dai limiti.");

        
        return ((data.get(exampleIndex).get(attributeIndex)));
    }

    /**
     * Restituisce una tupla di item all'indice specificato.
     * 
     * @param index l'indice dell'esempio
     * 
     * @return la tupla corrispondente all'esempio
     */
    public Tuple getItemSet(int index) {
        Tuple tuple = new Tuple(attributeSet.size());
        for (int i = 0; i < attributeSet.size(); i++)
        {
            Attribute a = attributeSet.get(i);
            Object v = data.get(index).get(i);

            if (a instanceof ContinuousAttribute)
                tuple.add(new ContinuousItem(a, ((Number) v).doubleValue()), i);
            else
                tuple.add(new DiscreteItem(a, (String) v), i);
        }
        return tuple;
    }
    
    /**
     * Restituisce una rappresentazione testuale del dataset.
     * 
     * @return una stringa contenente l'intero dataset in formato leggibile
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        for(Attribute attr : attributeSet)
            sb.append(attr.toString()).append(", ");

        sb.setLength(sb.length() - 2);
        sb.append("\n");
        
        for(int i = 0; i < numberOfExamples; i++)
        {
            sb.append(i).append(": ");
            for(int j = 0; j < attributeSet.size(); j++)
                sb.append(data.get(i).get(j)).append(", "); 
            
            sb.setLength(sb.length() - 2);
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Crea gli attributi a partire dalla tabella del database.
     * <p>E' un metodo privato, non accessibile al di fuori della classe.</p>
     * 
     * @param tableName nome della tabella
     * @param database accesso al database
     * @param tableSchema schema della tabella del database
     * 
     * @throws SQLException se ci sono errori nella lettura dello schema
     */
    private void createAttributesFromTableSchema(String tableName, DBAccess database, TableSchema tableSchema) throws SQLException
    {
        TableData tableData = new TableData(database);

        for(int i = 0; i < tableSchema.getNumberOfAttributes(); i++)
        {
            Column column = tableSchema.getColumn(i);
            String columnName = column.getColumnName();

            if(column.isNumber())
            {
                try
                {
                    Object minObj = tableData.getAggregateColumnValue(tableName, column, QUERY_TYPE.MIN);
                    Object maxObj = tableData.getAggregateColumnValue(tableName, column, QUERY_TYPE.MAX);

                    double min = 0.0, max = 0.0;

                    if(minObj instanceof Number)
                        min  = ((Number) minObj).doubleValue();
                    else if (minObj != null)
                        min = Double.parseDouble(minObj.toString());

                    if(maxObj instanceof Number)
                        max = ((Number) maxObj).doubleValue();
                    else if (minObj != null)
                        max = Double.parseDouble(maxObj.toString());

                    attributeSet.add(new ContinuousAttribute(columnName, i, min, max));
                }
                catch(NoValueException exception)
                {
                    System.err.println(exception.getMessage());
                }
            }
            else
            {
                Set<Object> distinctValues = tableData.getDistinctColumnValues(tableName, column);

                String[] values = new String[distinctValues.size()];

                
                int j = 0;
                for(Object v : distinctValues)
                    values[j++] = v.toString();
                
                attributeSet.add(new DiscreteAttribute(columnName, i, values));
            }
        }
    }
}
