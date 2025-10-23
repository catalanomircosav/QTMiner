package server;

import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.rmi.ServerException;

import mining.QTMiner;
import data.Data;

import exceptions.DatabaseConnectionException;
import exceptions.EmptyDatasetException;
import java.sql.SQLException;

public class ServerOneClient extends Thread {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private QTMiner kmeans;
    
    public ServerOneClient(Socket s) throws IOException
    {
        this.socket = s;

        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        
        System.out.println("Thread creato per: " + s.getInetAddress());
    }

    @Override
    public void run()
    {
        Data data = null;
        Double radius = null;
        String tname = "";
        try
        {
            while(!socket.isClosed())
            {
                switch((Integer) in.readObject())
                {
                    case 0:
                        System.out.println("Reading from database...");

                        tname = (String) in.readObject();

                        try
                        {
                            System.out.println("ciao 0");

                            data = new Data(tname);
                            System.out.println("ciao 1");
                            
                            out.writeObject("OK");
                        }
                        catch(Exception e)
                        {

                        }
                    break;
                    case 1:
                        System.out.println("Computing with radius...");

                        radius = (Double) in.readObject();
                        kmeans = new QTMiner(radius);

                        try
                        {
                            int comp = kmeans.compute(data);

                            out.writeObject("OK");
                            out.writeObject(comp);
                            out.writeObject(kmeans.getC().toString(data));
                        }
                        catch(Exception e)
                        {

                        }

                    break;
                        
                    case 2:
                        System.out.println("Saving on file...");

                        String filename = tname + "_" + radius + ".dmp";

                        kmeans.salva(filename);

                        out.writeObject("OK");
                    break;

                    case 3:
                        System.out.print("Reading from file...");
                        
                        filename = (String) in.readObject() + "_" + (double) in.readObject() + ".dmp";

                        try
                        {
                            kmeans = new QTMiner(filename);
                            out.writeObject("OK");
                            out.writeObject(kmeans.getC().toString());
                        }
                        catch(FileNotFoundException exception)
                        {
                            System.err.println(exception.getMessage());
                        }
                    break;

                    default: break;
                }
            }
        }
        catch(EOFException exception)
        {
            System.err.println(exception.getMessage());
        }
        catch(IOException | ClassNotFoundException exception)
        {
            System.err.println(exception.getMessage());
            exception.printStackTrace();
        }
        finally
        {
            try
            {
                socket.close();
                System.out.println("Connection with: " + socket.getInetAddress() + " closed successfully.");
            }
            catch(IOException exception)
            {
                System.err.println(exception.getMessage());
            }
        }
    }
}
