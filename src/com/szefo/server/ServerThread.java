package com.szefo.server;


import com.szefo.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread extends Thread {

    private Socket connection = null;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerFrame gui;
    private ServerSocketImpl serverSocketImpl = null;
    private int id = -1;
    private volatile boolean running = true;

    public ServerThread(Socket connection, ServerSocketImpl serverSocketImpl) {
        super();
        this.connection = connection;
        this.serverSocketImpl = serverSocketImpl;
        id = connection.getPort();
        gui = serverSocketImpl.getGui();
    }

    public int getID() {
        return id;
    }

    public Socket getConnection() {
        return connection;
    }

    @SuppressWarnings("deprecation")
    public void run() {
        gui.getjTextArea().append("\nServer Thread " + id + " running");
        while (running) {
            try {
                Message msg = (Message) input.readObject();
                serverSocketImpl.handle(id, msg);
            } catch (Exception e) {
                System.out.println(id + " Error reading : " + e.getMessage());
                serverSocketImpl.remove(id);
                stop();
            }
        }
    }

    public void send(Message message) {
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void open() {
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (connection != null) connection.close();
            if (output != null) output.close();
            if (input != null) input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void terminate(){
        running = false;
    }

}
