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
    private String msg;
    private int clientId;
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
        //last 100 messages (kada se implementira koriscenje liste u manageru)
        String greet = "Server: Još jedna osoba se pridružila konverzaciji.";
        for (ClientSocketHandler handler : Manager.getClients()) {
            handler.broadcast(greet);
        }
    }

    private synchronized void broadcast(String message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException ex) {
            System.out.println("Error broadcasting message: " + ex);
        }
    }

    @Override
    public void run() {
        init();
        try {
            while (connected) { //promenjeno bez testiranja!
                msg = (String) in.readObject();
                for (ClientSocketHandler handler : Manager.getClients()) {
                    handler.broadcast(msg);
                }
            }
        } catch (EOFException eofex) { //napravi elegantnije zatvaranje soketa i strimova klijenata
            try {
                System.out.println("Client closed app: " + eofex);
                out.close();
                in.close();
                client.close();
                Manager.getClients().remove(this);
                connected = false;
            } catch (IOException ex) {
                System.out.println("Error closing stram and socket: " + ex);
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex);
        }
        
    }

}
