package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSocketHandler implements Runnable {

    private final Socket client;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Message msg;
    private int clientId;
    
    public ClientSocketHandler(Socket client, int clientId) {
        this.client = client;
        this.clientId = clientId;
        msg = new Message("Server", "Iniitial message"); //dodato za inicijalizaciju
        initInOutStream(client);
    }

    private void initInOutStream(Socket client1) {
        try {
            out = new ObjectOutputStream(client1.getOutputStream());
//            out.flush();
            in = new ObjectInputStream(client1.getInputStream());
            System.out.println("--stream established");
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    private void init() {
        //last 100 messages (kada se implementira koriscenje liste u manageru)
        //dodati preuzimanje imena sa Client
        Message greet = new Message("Server", "Još jedna osoba se pridružila konverzaciji.");
        for (ClientSocketHandler handler : Manager.getClients()) {
            handler.broadcast(greet);
        }
    }

    private synchronized void broadcast(Message message) {
        try {
            out.writeObject(message);
//            out.flush();
        } catch (IOException ex) {
            System.out.println("Error broadcasting message: " + ex);
        }
    }

    @Override
    public void run() {
//        init();
        while (!msg.getText().equals("logout")) {
            try {
                msg = (Message) in.readObject();
                for (ClientSocketHandler handler : Manager.getClients()) {
                    handler.broadcast(msg);
                }
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println(ex);
            }
        }
    }

}
