package com.szefo.client;


import com.szefo.Message;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;

public class ClientSocketImpl implements Runnable {

    private int port;
    private String serverAddress;
    private Socket socket;
    private ClientFrame gui;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private History history;
    private volatile boolean running = true;

    public ClientSocketImpl(ClientFrame clientFrame) throws IOException {
        this.gui = clientFrame;
        this.serverAddress = gui.getServerAddress();
        this.port = gui.getPort();

        this.socket = new Socket(InetAddress.getByName(serverAddress), port);

        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.output.flush();
        this.input = new ObjectInputStream(socket.getInputStream());

        this.history = gui.getHistory();
    }

    public void send(Message message) {
        try {
            output.writeObject(message);
            output.flush();
            System.out.println("Outgoing : " + message.toString());
            if (message.equals("message") && !message.getContent().equals(".bye")) {
                String msgTime = new Date().toString();
                history.addMessage(message, msgTime);
                DefaultTableModel table = (DefaultTableModel) gui.getHistoryFrame().getjTable().getModel();
                table.addRow(new Object[]{"Me", message.getContent(), message.getRecipient(), msgTime});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                Message message = (Message) input.readObject();
                System.out.println("Incoming : " + message.toString());
                if (message.getType().equals("message")) {
                    if (message.getRecipient().equals(gui.getUsername())) {
                        gui.getjTextArea().append("[" + message.getSender() + " > Me] : " + message.getContent() + "\n");
                    } else {
                        gui.getjTextArea().append("[" + message.getSender() + " > " + message.getRecipient() + "] : " + message.getContent() + "\n");
                    }

                    if (!message.getContent().equals(".bye") && !message.getSender().equals(gui.getUsername())) {
                        String msgTime = (new Date()).toString();

                        try {
                            history.addMessage(message, msgTime);
                            DefaultTableModel table = (DefaultTableModel) gui.getHistoryFrame().getjTable().getModel();
                            table.addRow(new Object[]{message.getSender(), message.getContent(), "Me", msgTime});
                        } catch (Exception ex) {
                        }
                    }
                } else if (message.getType().equals("login")) {
                    if (message.getContent().equals("TRUE")) {
                        gui.getjBLogin().setEnabled(false);
                        gui.getjBSignup().setEnabled(false);
                        gui.getjBSendMessage().setEnabled(true);
                        gui.getjBBrowse().setEnabled(true);
                        gui.getjTextArea().append("[SERVER > Me] : Login Successful\n");
                        gui.getjTUserName().setEnabled(false);
                        gui.getjPasswordField().setEnabled(false);
                    } else {
                        gui.getjTextArea().append("[SERVER > Me] : Login Failed\n");
                    }
                } else if (message.getType().equals("test")) {
                    gui.getjBConnect().setEnabled(false);
                    gui.getjBLogin().setEnabled(true);
                    gui.getjBSignup().setEnabled(true);
                    gui.getjTUserName().setEnabled(true);
                    gui.getjPasswordField().setEnabled(true);
                    gui.getjTHostAddress().setEditable(false);
                    gui.getjTHostPort().setEditable(false);
                    gui.getjBBrowse2().setEnabled(true);
                } else if (message.getType().equals("newuser")) {
                    if (!message.getContent().equals(gui.getUsername())) {
                        boolean exists = false;
                        for (int i = 0; i < gui.getModel().getSize(); i++) {
                            if (gui.getModel().getElementAt(i).equals(message.getContent())) {
                                exists = true;
                                break;
                            }
                        }
                        if (!exists) {
                            gui.getModel().addElement(message.getContent());
                        }
                    }
                } else if (message.getType().equals("signup")) {
                    if (message.getContent().equals("TRUE")) {
                        gui.getjBLogin().setEnabled(false);
                        gui.getjBSignup().setEnabled(false);
                        gui.getjBSendMessage().setEnabled(true);
                        gui.getjBBrowse().setEnabled(true);
                        gui.getjTextArea().append("[SERVER > Me] : Singup Successful\n");
                    } else {
                        gui.getjTextArea().append("[SERVER > Me] : Signup Failed\n");
                    }
                } else if (message.getType().equals("signout")) {
                    if (message.getContent().equals(gui.getUsername())) {
                        gui.getjTextArea().append("[" + message.getSender() + " > Me] : Bye\n");
                        gui.getjBConnect().setEnabled(true);
                        gui.getjBSendMessage().setEnabled(false);
                        gui.getjTHostAddress().setEditable(true);
                        gui.getjTHostPort().setEditable(true);

                        for (int i = 1; i < gui.getModel().size(); i++) {
                            gui.getModel().removeElementAt(i);
                        }

                        gui.getClientThread().stop();
                    } else {
                        gui.getModel().removeElement(message.getContent());
                        gui.getjTextArea().append("[" + message.getSender() + " > All] : " + message.getContent() + " has signed out\n");
                    }
                } else if (message.getType().equals("upload_req")) {

                    if (JOptionPane.showConfirmDialog(gui, ("Accept '" + message.getContent() + "' from " + message.getSender() + " ?")) == 0) {

                        JFileChooser jf = new JFileChooser();
                        jf.setSelectedFile(new File(message.getContent()));
                        int returnVal = jf.showSaveDialog(gui);

                        String saveTo = jf.getSelectedFile().getPath();
                        if (saveTo != null && returnVal == JFileChooser.APPROVE_OPTION) {
                            Download dwn = new Download(saveTo, gui);
                            Thread t = new Thread(dwn);
                            t.start();
                            //send(new Message("upload_res", (""+InetAddress.getLocalHost().getHostAddress()), (""+dwn.port), message.getSender()));
                            send(new Message("upload_res", gui.getUsername(), ("" + dwn.getPort()), message.getSender()));
                        } else {
                            send(new Message("upload_res", gui.getUsername(), "NO", message.getSender()));
                        }
                    } else {
                        send(new Message("upload_res", gui.getUsername(), "NO", message.getSender()));
                    }
                } else if (message.getType().equals("upload_res")) {
                    if (!message.getContent().equals("NO")) {
                        int port = Integer.parseInt(message.getContent());
                        String addr = message.getSender();

                        gui.getjBBrowse().setEnabled(false);
                        gui.getjBSend().setEnabled(false);
                        Upload upl = new Upload(addr, port, gui.getFile(), gui);
                        Thread t = new Thread(upl);
                        t.start();
                    } else {
                        gui.getjTextArea().append("[SERVER > Me] : " + message.getSender() + " rejected file request\n");
                    }
                } else {
                    gui.getjTextArea().append("[SERVER > Me] : Unknown message type\n");
                }
            } catch (Exception ex) {
                running = false;
                gui.getjTextArea().append("[Application > Me] : Connection Failure\n");
                gui.getjBConnect().setEnabled(true);
                gui.getjTHostAddress().setEditable(true);
                gui.getjTHostPort().setEditable(true);
                gui.getjBSendMessage().setEnabled(false);
                gui.getjBBrowse().setEnabled(false);


                for (int i = 1; i < gui.getModel().size(); i++) {
                    gui.getModel().removeElementAt(i);
                }

                gui.getClientThread().stop();

                System.out.println("Exception SocketClient run()");
                ex.printStackTrace();
            }
        }
    }

    public void closeThread(Thread t){
        t = null;
    }
}
