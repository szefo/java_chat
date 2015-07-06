package com.szefo.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServerSocketImpl implements Runnable {

    private int port = 1234;
    private List<ServerThread> threads = null;
    private ServerSocket serverSocket = null;
    private Thread thread = null;
    private int clientCount = 0;
    private ServerFrame gui;
    private DatabaseXml db;

    public ServerSocketImpl(ServerFrame frame) {
        threads = new LinkedList<>();
        this.gui = frame;
        // TODO
        db = new DatabaseXml("");

        try {
            serverSocket = new ServerSocket(port);
            gui.getjTextArea().append("Server is working. IP : "
                    + InetAddress.getLocalHost() + ", Port : "
                    + serverSocket.getLocalPort());
            start();
        } catch (IOException e) {
            gui.getjTextArea().append("\nCan not bind to port"
                    + port + ": " + e.getMessage());
            gui.retryStart(0);
        }
    }

    public ServerSocketImpl(ServerFrame serverFrame, int port) {
        threads = new LinkedList<>();
        this.gui = serverFrame;
        this.port = port;
        // TODO
        db = new DatabaseXml("");

        try {
            serverSocket = new ServerSocket(port);
            gui.getjTextArea().append("Server is working. IP : "
                    + InetAddress.getLocalHost() + ", Port : "
                    + serverSocket.getLocalPort());
            start();
        } catch (IOException e) {
            gui.getjTextArea().append("\nCan not bind to port"
                    + port + ": " + e.getMessage());
        }
    }

    public ServerFrame getGui() {
        return gui;
    }

    private void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    @SuppressWarnings("deprecation")
    public void stop() {
        if (thread != null) {
            thread.stop();
            thread = null;
        }
    }

    // TODO Sets
    private int findClient(int id) {
        for (int i = 0; i < clientCount; i++) {
            if (threads.iterator().next().getID() == id) {
                return id;
            }
        }
        return -1;
    }

    // TODO
    public synchronized void handle(int id, Message msg) {
        if (msg.getContent().equals(".bye")) {
            Announce("signout", "SERVER", msg.getSender());
            remove(id);
        } else {
            if (msg.getType().equals("login")) {
                if (findUserThread(msg.getSender()) == null) {
                    if (db.checkLogin(msg.getSender(), msg.getContent())) {
                        // threads set to do
                    }
                }
            } else if (msg.getType().equals("message")) {

            } else if (msg.getType().equals("test")){

            } else if (msg.getType().equals("signup")){

            } else if (msg.getType().equals("upload_req")){

            } else if (msg.getType().equals("upload_res")){

            }
        }
    }

    // TODO Sets iterator I don't know if that works properly
    public void Announce(String type, String sender, String content) {
        Message msg = new Message(type, sender, content, "All");
        for (int i = 0; i < clientCount; i++) {
            threads.iterator().next().send(msg);
        }
    }

    // TODO
    public void SendUserList(String toWhom) {

    }

    // TODO
    public ServerThread findUserThread(String usr) {

        return null;
    }

    // TODO
    public synchronized void remove(int id) {

    }


    @Override
    public void run() {
        while (thread != null) {
            try {
                gui.getjTextArea().append("\nWaiting for a client ...");
                addThread(serverSocket.accept());
            } catch (IOException e) {
                gui.getjTextArea().append("\nServer accept error: \n");
                gui.retryStart(0);
            }
        }
    }

    // TODO
    private void addThread(Socket socket) {

        if (clientCount < threads.size()) {
            gui.getjTextArea().append("\nClient accepted: " + socket);
            threads.add(new ServerThread(socket, this));
            threads.get(threads.size()).open();
            threads.get(threads.size()).start();
            clientCount++;
        } else {
            gui.getjTextArea().append("\nClient refused: maximum " + threads.size());
        }
    }


}
