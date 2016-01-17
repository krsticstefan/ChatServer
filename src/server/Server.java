package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static final int PORT = 9000;
    ServerSocket ss;
    private static int clientCounter = 0;

    private static List<ClientSocketHandler> clientsOfServer;

    public Server() {
        startServer();
    }

    private void startServer() {
        clientsOfServer = new ArrayList<>();
        try {
            ss = new ServerSocket(9000);
            System.out.println("Server running; \nHost IP is " + InetAddress.getLocalHost() + ";\nListening on port " + PORT);
            while (true) {
                Socket clientSocket = ss.accept();
                System.out.println("Client accepted: " + ++clientCounter);
                ClientSocketHandler csh = new ClientSocketHandler(clientSocket, clientCounter);
                Thread serverThread = new Thread(csh, "client[" + clientCounter + ']');
//                Manager.addClient(csh);
                clientsOfServer.add(csh); //umesto da radi iz Manager-a
                serverThread.start();
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
