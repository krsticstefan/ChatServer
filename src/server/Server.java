package server;

import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server { //dodaj jednostavan GUI, sa prikazom broja klijenata i svih poruka, kao i opciju isključivanja servera

    private static final int PORT = 9000;
    ServerSocket ss;
    private static int clientCounter = 0;

    public Server() {
        startServer();
    }

    private void startServer() {
        try {
            ss = new ServerSocket(9000);
            System.out.println("Server running; \nHost IP is " + InetAddress.getLocalHost() + ";\nListening on port " + PORT);
            while (true) {
                Socket clientSocket = ss.accept();
                ClientSocketHandler csh = new ClientSocketHandler(clientSocket, ++clientCounter);
                System.out.println("Client accepted: " + clientCounter);
                Thread serverThread = new Thread(csh, "client[" + clientCounter + ']');
                Manager.addClient(csh);
                serverThread.start();
            }
        } catch (EOFException eofex) {
            System.out.println("Client terminated connection: " + eofex);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
