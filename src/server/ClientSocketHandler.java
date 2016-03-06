package server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSocketHandler implements Runnable {
    
    private final Socket client;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String msg, username;
    private final int clientId;
    private boolean connected;

    public ClientSocketHandler(Socket client, int clientId) {
        this.client = client;
        this.clientId = clientId;
        msg = "Server: Initial message";
        initInOutStream(client);
    }

    private void initInOutStream(Socket sock) {
        try {
            out = new ObjectOutputStream(sock.getOutputStream());
            out.flush();
            in = new ObjectInputStream(sock.getInputStream());
            connected = true;
            System.out.println("--stream established");
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    private void init() {
        String greet = " has joined the conversation!";
        try { //adding a new client
            username = (String) in.readObject();
            Manager.addClient(username, this);
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("init: " + ex);
        }
        try { //sending messages to new client
            for (String msg : Manager.getMESSAGES()) {
                out.writeObject(msg);
            }
        } catch (IOException ex) {
            Server.serverlog("\nError loading messages.");
            System.out.println("init: " + ex);
        }
        Manager.newMessage(username + greet);
        for (ClientSocketHandler handler : Manager.getCLIENTS().values()) {
            handler.broadcast(username + greet);
        }
    }

    private synchronized void broadcast(String message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException ex) {
            System.out.println("broadcast: " + ex);
            Server.serverlog("\nError broadcasting message.");
        }
    }

    @Override
    public void run() {
        init();
        try {
            while (connected) {
                msg = (String) in.readObject();
                if (msg.contains("end123<>!")) {
                    msg = username + " has left the conversation.";
                    Manager.removeClient(username);
                    Server.clientLeft();
                }
                Manager.newMessage(msg);
                for (ClientSocketHandler handler : Manager.getCLIENTS().values()) {
                    handler.broadcast(msg);
                }
            }
        } catch (EOFException eofex) {
            try {
                System.out.println("Client closed app: " + eofex);
                Manager.getCLIENTS().remove(username);
                out.close();
                in.close();
                client.close();
                connected = false;
            } catch (IOException ex) {
                System.out.println("Error closing stram and socket: " + ex);
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex);
        }

    }

}
