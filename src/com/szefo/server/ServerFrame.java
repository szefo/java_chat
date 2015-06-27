package com.szefo.server;

import javax.swing.*;

public class ServerFrame extends JFrame {

    private JButton jBstart, jBbrowse;
    private JScrollPane jScrollPane;
    private JTextArea jTextArea;
    private JTextField jTextField;
    private JLabel jLabel;
    private JFileChooser jFileChooser;

    private SocketServer socketServer;
    private final String filePath = "";
    private Thread serverThread;

    public ServerFrame(){
        initComponents();
        setDefaultCloseOperation(3);
        setTitle("ServerFrame");
    }

    private void initComponents() {



    }

    public boolean isWin32(){
        return System.getProperty("os.name").startsWith("Windows");
    }

}
