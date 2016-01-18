package server;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author stefan
 */
public class Manager {

//    private static List<Message> msgs;
    private static final List<ClientSocketHandler> clients;

    static {
        clients = new ArrayList<>();
    }

    public static void addClient(ClientSocketHandler csh) {
        clients.add(csh);
    }

    public static void newMessage() {
        //cuva poslednjh 100
    }

    public static List<ClientSocketHandler> getClients() {
        return clients;
    }
}
