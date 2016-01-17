package server;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author stefan
 */
public class Manager {

    private static List<Message> msgs;
    private static List<ClientSocketHandler> clients;

    public Manager() {
        clients = new ArrayList<>(10);
    }

    /**
     *
     * @param csh
     */
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
