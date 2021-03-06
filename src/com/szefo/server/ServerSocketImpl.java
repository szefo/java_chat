package com.szefo.server;

import com.szefo.DatabaseXml;
import com.szefo.Message;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketImpl implements Runnable {

    private int port = 1234;
    private ServerThread[] threads = null;
    private ServerSocket serverSocket = null;
    private Thread thread = null;
    private int clientCount = 0;
    private ServerFrame gui;
    private DatabaseXml db;
    private volatile boolean running = true;

    public ServerSocketImpl(ServerFrame frame) {
        threads = new ServerThread[50];
        this.gui = frame;

        db = new DatabaseXml(gui.getFilePath());

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
        threads = new ServerThread[50];
        this.gui = serverFrame;
        this.port = port;

        db = new DatabaseXml(gui.getFilePath());

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

    public void stop() {
        if (thread != null) {
            terminate();
            thread = null;
        }
    }


    public synchronized void handle(int id, Message msg) {
        if (msg.getContent().equals(".bye")) {
            announce("signout", "SERVER", msg.getSender());
            remove(id);
        } else {
            if (msg.getType().equals("login")) {
                if (findUserThread(msg.getSender()) == null) {
                    if (db.checkLogin(msg.getSender(), msg.getContent())) {
                        threads[(findClient(id))].setName(msg.getSender());
                        threads[(findClient(id))].send(new Message("login", "SERVER", "TRUE", msg.getSender()));
                        announce("newuser", "SERVER", msg.getSender());
                        sendUserList(msg.getSender());
                    }
                }
            } else if (msg.getType().equals("message")) {
                if (msg.getRecipient().equals("All")) {
                    announce("message", msg.getSender(), msg.getContent());
                } else {
                    findUserThread(msg.getRecipient()).send(
                            new Message(msg.getType(), msg.getSender(), msg.getContent(), msg.getRecipient()));
                    threads[findClient(id)].send(
                            new Message(msg.getType(), msg.getSender(), msg.getContent(), msg.getRecipient()));
                }
            } else if (msg.getType().equals("test")) {
                threads[findClient(id)].send(
                        new Message(msg.getType(), msg.getSender(), msg.getContent(), msg.getRecipient()));
            } else if (msg.getType().equals("signup")) {
                if (findUserThread(msg.getSender()) == null) {
                    if (!db.userExists(msg.getSender())) {
                        db.addUser(msg.getSender(), msg.getContent());
                        threads[findClient(id)].setName(msg.getSender());
                        threads[findClient(id)].send(
                                new Message("signup", "SERVER", "TRUE", msg.getSender()));
                        threads[findClient(id)].send(
                                new Message("login", "SERVER", "TRUE", msg.getSender()));
                        announce("newuser", "SERVER", msg.getSender());
                        sendUserList(msg.getSender());
                    } else {
                        threads[findClient(id)].send(
                                new Message("signup", "SERVER", "FALSE", msg.getSender()));
                    }
                } else {
                    threads[findClient(id)].send(
                            new Message("signup", "SERVER", "FALSE", msg.getSender()));
                }
            } else if (msg.getType().equals("upload_req")) {
                if (msg.getRecipient().equals("All")) {
                    threads[findClient(id)].send(
                            new Message("message", "SERVER", "Upload to 'All' forbiden", msg.getSender()));
                } else {
                    findUserThread(msg.getRecipient()).send(
                            new Message("upload_req", msg.getSender(), msg.getContent(), msg.getRecipient()));
                }
            } else if (msg.getType().equals("upload_res")) {
                if (!msg.getContent().equals("NO")) {
                    String IP = findUserThread(msg.getSender()).getConnection().getInetAddress().getHostAddress();
                    findUserThread(msg.getRecipient()).send(
                            new Message("upload_res", IP, msg.getContent(), msg.getRecipient()));
                } else {
                    findUserThread(msg.getRecipient()).send(
                            new Message("upload_res", msg.getSender(), msg.getContent(), msg.getRecipient()));
                }
            }
        }
    }

    public void announce(String type, String sender, String content) {
        Message msg = new Message(type, sender, content, "All");
        for (ServerThread st : threads)
            st.send(msg);
    }

    public void sendUserList(String toWhom) {
        for (int i = 0; i < clientCount; i++) {
            findUserThread(toWhom).send(new Message("newuser", "SERVER", threads[i].getName(), toWhom));
        }
    }

    public ServerThread findUserThread(String usr) {
        for (int i = 0; i < clientCount; i++) {
            if (threads[i].getName().equals(usr)) {
                return threads[i];
            }
        }
        return null;
    }

    private int findClient(int id) {
        for (int i = 0; i < clientCount; i++) {
            if (threads[i].getID() == id) {
                return i;
            }
        }
        return -1;
    }

    public synchronized void remove(int id) {
        int pos = findClient(id);
        ServerThread toTerminate = null;
        if (pos >= 0) {
            gui.getjTextArea().append("\nRemoving client thread " + id + " at " + pos);
            toTerminate = threads[pos];
        }
        if (pos < clientCount - 1) {
            for (int i = pos + 1; i < clientCount; i++) {
                threads[i - 1] = threads[i];
            }
        }
        clientCount--;
        toTerminate.close();
        toTerminate.terminate();
    }

    @Override
    public void run() {
        while (running && thread != null) {
            try {
                gui.getjTextArea().append("\nWaiting for a client ...");
                addThread(serverSocket.accept());
            } catch (IOException e) {
                gui.getjTextArea().append("\nServer accept error: \n");
                gui.retryStart(0);
            }
        }
    }

    private void addThread(Socket socket) {
        if (clientCount < threads.length) {
            gui.getjTextArea().append("\nClient accepted: " + socket);
            threads[clientCount] = new ServerThread(socket, this);
            threads[clientCount].open();
            threads[clientCount].start();
            clientCount++;
        } else {
            gui.getjTextArea().append("\nClient refused: maximum " + threads.length);
        }
    }

    public void terminate() {
        running = false;
    }

}
