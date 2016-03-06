package server;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Server extends JFrame implements ActionListener {

    /* TODO:
     * odvojiti grafiku u posebnoj klasi
     */
    
    private static Thread t;
    private static final int PORT = 9000;
    private static ServerSocket ss;
    private static int clientCounter = 0;
    private static boolean up;

    private static JPanel serverPnl;
    private static JButton startBtn, stopBtn;
    private static JTextArea konzolTxt;

    public Server() {
        initGUI();
        up = true;
    }
    
    public static void serverlog(String msg){
        konzolTxt.append(msg);
    }
    
    public static void clientLeft(){
        konzolTxt.append("\nA client terminated connection, number of clients: " + --clientCounter);
    }

    private void initGUI() {
        serverPnl = new JPanel(new FlowLayout(FlowLayout.CENTER));
        konzolTxt = new JTextArea(20, 50);
        startBtn = new JButton("Start");
        stopBtn = new JButton("Close");
        serverPnl.add(new JScrollPane(konzolTxt));
        this.add(serverPnl);
        serverPnl.add(konzolTxt);
        serverPnl.add(startBtn);
        serverPnl.setVisible(true);
        startBtn.addActionListener(this);
        stopBtn.addActionListener(this);
        setSize(600, 400);
        setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        konzolTxt.append("Welcome!");
    }

    private void startServer() {
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ss = new ServerSocket(9000);
                    konzolTxt.append("\nServer running; \nHost is "
                            + InetAddress.getLocalHost() + ";\nListening on port " + PORT);
                    while (up) {
                        Socket clientSocket = ss.accept();
                        ClientSocketHandler csh = new ClientSocketHandler(clientSocket, ++clientCounter);
                        konzolTxt.append("\nClient accepted: " + clientCounter);
                        Thread serverThread = new Thread(csh, "client[" + clientCounter + ']');
                        serverThread.start();
                    }
                } catch (BindException ex) {
                    konzolTxt.append("\nBusy! Port is in use: " + ex);
                    System.out.println("startServer: " + ex);
                } catch (IOException ex) {
                    System.out.println("startServer: " + ex);
                }
            }
        });
        t.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("Start")) {
            konzolTxt.append("\n" + "Starting server.");
            serverPnl.add(stopBtn);
            startBtn.setVisible(false);
            startServer();
        } else if (e.getActionCommand().equalsIgnoreCase("Close")) {
            stopServer();
            System.exit(0);
        }
    }

    private void stopServer() {
        try {
            konzolTxt.append("\n" + "Saving messages.");
            Manager.cleanup();
            up = false;
            konzolTxt.append("\n" + "Stopping server.");
            ss.close();
            t.interrupt();
        } catch (IOException ex) {
            System.out.println("stopServer" + ex);
        }
    }
}
