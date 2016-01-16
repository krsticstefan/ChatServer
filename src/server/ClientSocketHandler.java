package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSocketHandler implements Runnable {

    private final Socket client;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ClientSocketHandler(Socket client) {
        this.client = client;
        initInOutStream(client);
    }

    private void initInOutStream(Socket client1) {
        try {
            in = new ObjectInputStream(client1.getInputStream());
            out = new ObjectOutputStream(client1.getOutputStream());
            System.out.println("--stream established");
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void init() {
        //last 100 messages
        //send an instruction message for logout
    }

    public void listen() {
        Message msg = null;
        do {
            try {
                msg = (Message) in.readObject();
                //prosledi u memoriju
                for (ClientSocketHandler handler : Manager.getClients()) {
                    handler.broadcast(msg);
                }
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println(ex);
            }
        } while (!msg.getText().equals("logout"));
    }

    public synchronized void broadcast(Message message) throws IOException {
        out.writeObject(message);
    }

    @Override
    public void run() {
        init();
        while (true) {
            listen();
        }
    }

}
