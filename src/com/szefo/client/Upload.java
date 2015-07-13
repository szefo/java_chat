package com.szefo.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by szefo on 07.07.15.
 */
public class Upload implements Runnable {

    private String address;
    private int port;
    private Socket socket;
    private FileInputStream input;
    private OutputStream output;
    private File file;
    private ClientFrame gui;


    public Upload(String address, int port, File file, ClientFrame gui) {
        super();
        this.address = address;
        this.port = port;
        this.file = file;
        this.gui = gui;
        try {
            socket = new Socket(InetAddress.getByName(address), port);
            output = socket.getOutputStream();
            input = new FileInputStream(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[1024];
            int count;

            while ((count = input.read(buffer)) >= 0) {
                output.write(buffer, 0, count);
            }
            output.flush();

            gui.getjTextArea().append("[Application > Me]: File upload complete\n");
            gui.getjBSend().setEnabled(true);
            gui.getjBBrowse().setEnabled(true);
            gui.getjTFileBrowse().setVisible(true);

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
            System.out.println("Exception [Upload : run()]");
            e.printStackTrace();
        }
    }
}
