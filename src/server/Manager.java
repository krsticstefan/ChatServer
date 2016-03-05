package server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {

    /* TODO:
     * umesto String sasatviti poruku od username, datum, poruka (Message.java) (?)
     * ograniciti listu poruka na 100, ako klijent zadrazi prikaz starijih poruka, izvuci iz txt
     */
    
    /*
        If you want to serialize the ArrayList object to a file so you can read it back in again
        later use ObjectOuputStream/ObjectInputStream writeObject()/readObject() 
        since ArrayList implements Serializable. It's not clear to me from y
        our question if you want to do this or just write each individual item. 
        If so then Andrey's answer will do that.
     */
    
    private static final List<String> MESSAGES;
    private static final Map<String, ClientSocketHandler> CLIENTS;
    private static int messageCounter;
    private static final File MSGSTORAGE;
    private static FileWriter MSGWRITER;

    static {
        messageCounter = 0;
        CLIENTS = new HashMap<>();
        MESSAGES = new ArrayList<>();
        MSGSTORAGE = new File("messages.txt");
        try {
            MSGWRITER = new FileWriter(MSGSTORAGE);
        } catch (IOException ex) {
            System.out.println("Error opening file for writing: " + ex);
        }
    }

    private static void writeback() {
        if (MESSAGES.size() < 10) {
            for (String str : MESSAGES) {
                try {
                    MSGWRITER.write(str + "\n");
                } catch (IOException ex) {
                    System.out.println("Error writing messages on file." + ex);
                }
            }
        } else {
            for (int i = MESSAGES.size() - 9; i != MESSAGES.size(); i++) {
                try {
                    MSGWRITER.append(MESSAGES.get(i) + "\n");
                } catch (IOException ex) {
                    System.out.println("Error writing messages on file." + ex);
                }
            }
        }
    }

    public static void cleanup() {
        writeback();
        try {
            MSGWRITER.flush();
            MSGWRITER.close();
        } catch (IOException ex) {
            System.out.println("Error closing FileWriter: " + ex);
        }
    }

    public static void addClient(String username, ClientSocketHandler csh) {
        CLIENTS.put(username, csh);
    }

    public static void removeClient(String usr) {
        CLIENTS.remove(usr);
    }

    public static Map<String, ClientSocketHandler> getCLIENTS() {
        return CLIENTS;
    }

    public static void newMessage(String msg) {
        messageCounter++;
        MESSAGES.add(msg);
        if (messageCounter == 10) {
            writeback();
            messageCounter = 0;
        }
    }

    public static List<String> getMESSAGES() {
        return MESSAGES;
    }

    public static int getMessageCounter() {
        return messageCounter;
    }
}
