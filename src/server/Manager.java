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
        clients = new ArrayList<>();
    }

    public static List<ClientSocketHandler> getClients() {
        return clients;
    }
}
