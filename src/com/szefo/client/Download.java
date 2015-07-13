package com.szefo.client;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Download implements Runnable {

    private ServerSocket serverSocket;
    private Socket socket;
    private int port;
    private String saveTo;
    private InputStream input;
    private FileOutputStream output;
    private ClientFrame gui;

    public Download(String saveTo, ClientFrame gui) {
        try {
            this.serverSocket = new ServerSocket(0);
            this.port = serverSocket.getLocalPort();
            this.saveTo = saveTo;
            this.gui = gui;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            socket = serverSocket.accept();
            System.out.println("Download : " + socket.getRemoteSocketAddress());

            input = socket.getInputStream();
            output = new FileOutputStream(saveTo);

            byte[] buffer = new byte[1024];
            int count;

            while ((count = input.read(buffer)) >= 0) {
                output.write(buffer, 0, count);
            }
            output.flush();

            gui.getjTextArea().append("[Application > Me] : Download complete\n");
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
            if (socket != null) {
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return port;
    }
}
