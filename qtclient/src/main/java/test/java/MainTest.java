package test.java;

import java.net.Socket;
import java.net.InetAddress;
import java.net.SocketException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;

import keyboardinput.Keyboard;

public class MainTest {

    private ObjectOutputStream out;
    private ObjectInputStream in;
    
    
    public MainTest(String ip, int port) throws IOException
    {
        InetAddress addr = InetAddress.getByName(ip);
        System.out.println("addr = " + addr);

        Socket socket = new Socket(addr, port);
        System.out.println(socket);
        
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }
    
    private int menu()
    {
        int answer = 1;
        
        try
        {
            do
            {
                System.out.println("(0) Carica cluster dal db");
                System.out.println("(1) Compute");
                System.out.println("(2) Salva cluster in file");
                System.out.println("(3) Leggi da file");
                System.out.print("(0/1/2/3):");

                answer = Keyboard.readInt();
            } while(answer < 0 || answer > 3);

            out.writeObject((Integer) answer);
        }
        catch(IOException exception)
        {
            System.err.println("Errore menu: " + exception.getMessage());
        }

        return answer;
    }

    private void storeTableFromDb() throws SocketException, ServerException, IOException, ClassNotFoundException
    {
        System.out.print("Nome tabella:");
        String tabName=Keyboard.readString();
        out.writeObject(tabName);

        String result = (String)in.readObject();
        if(!result.equals("OK"))
            throw new ServerException(result);
    }

    private String learningFromDbTable() throws SocketException, ServerException, IOException, ClassNotFoundException
    {
        double r = 1.0;
        do
        {
            System.out.print("Radius:");
            r = Keyboard.readDouble();
        } while(r <= 0);
        out.writeObject(r);

        String result = (String)in.readObject();
        if(result.equals("OK"))
        {
            System.out.println("Number of Clusters:"+ in.readObject());
            return (String)in.readObject();
        } else
            throw new ServerException(result);
    }

    private void storeClusterInFile() throws SocketException, ServerException, IOException, ClassNotFoundException
    {
        String result = (String)in.readObject();
        if(!result.equals("OK"))
            throw new ServerException(result);
    }

    private String learningFromFile() throws SocketException, ServerException, IOException, ClassNotFoundException
    {
        System.out.print("Nome file: ");
        String fileName = Keyboard.readString();
        out.writeObject(fileName);

        String result = (String) in.readObject();
        if(result.equals("OK"))
            return (String) in.readObject();
        else throw new ServerException(result);
    }

    public static void main(String[] args) 
    {
        if (args.length < 2) {
            System.out.println("Uso: java MainTest <ip> <porta>");
            return;
        }

        String ip = args[0];
        int port = Integer.parseInt(args[1]);

        MainTest main = null;

        try
        {
            main = new MainTest(ip,port);
        }
        catch (IOException exception)
        {
            System.err.println(exception.getMessage());
            return;
        }
        
        do
        {
            int menuAnswer = main.menu();
            try {
                switch(menuAnswer)
                {
                    case 0:
                        try
                        {
                            String clusters = main.learningFromFile();
                            System.out.println("Cluster caricati da file:");
                            System.out.println(clusters);
                        }
                        catch (Exception exception)
                        {
                            System.err.println("Errore: " + exception.getMessage());
                        }
                        break;

                    case 1: // Carica cluster dal db
                        try
                        {
                            String clusters = main.learningFromDbTable();
                            System.out.println("Cluster dal database:");
                            System.out.println(clusters);
                        }
                        catch (Exception exception)
                        {
                            System.err.println("Errore: " + exception.getMessage());
                        }
                        break;

                    case 2: // Salva cluster in file
                        try
                        {
                            main.storeClusterInFile();
                            System.out.println("Cluster salvati in file con successo");
                        }
                        catch (Exception exception)
                        {
                            System.err.println("Errore: " + exception.getMessage());
                        }
                        break;

                    case 3: // Store table from DB
                        try
                        {
                            main.storeTableFromDb();
                            System.out.println("Tabella verificata con successo");
                        }
                        catch (Exception exception)
                        {
                            System.err.println("Errore: " + exception.getMessage());
                        }
                        break;
                    
                    default:
                        System.out.println("Opzione non valida!");
                        break;
                }
            } catch (Exception e) {
                System.err.println("Errore generale: " + e.getMessage());
            }
            
            System.out.print("Vuoi scegliere un'altra operazione dal menu? (s/n): ");
            if(Keyboard.readChar() != 's') break;
        }
        while(true);

        try {
            main.out.close();
            main.in.close();
        } catch (IOException e) {
            System.err.println("Errore chiusura connessione: " + e.getMessage());
        }
    }
}