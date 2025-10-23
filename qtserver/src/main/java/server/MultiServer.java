package server;

import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class MultiServer {
    private int PORT = 8080;

    MultiServer(int port)
    {
        this.PORT = port;
        run();
    }

    void run()
    {
        ServerSocket serverSocket = null;
        
        try
        {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server in ascolto sulla porta: " + PORT);

            while(true)
            {
                try
                {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Nuova connessione da: " + clientSocket.getInetAddress());

                    new ServerOneClient(clientSocket).start();
                }
                catch(IOException exception)
                {
                    System.err.println(exception.getMessage());
                }
            }
        }
        catch(IOException exception)
        {
            System.err.println(exception.getMessage());
        }
        finally
        {
            if(serverSocket != null)
            {
                try
                {
                    serverSocket.close();
                }
                catch(IOException exception)
                {
                    System.err.println(exception.getMessage());
                }
            }
        }
    }

    public static void main(String[] args)
    {
        int port = 8080;
        if(args.length > 0)
        {
            try
            {
                port = Integer.parseInt(args[0]);
            }
            catch(NumberFormatException exception)
            {
                System.err.println(exception.getMessage());
            }
        }

        new MultiServer(port);
    }
}