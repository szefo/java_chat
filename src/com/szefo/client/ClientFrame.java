package com.szefo.client;

import com.szefo.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

public class ClientFrame extends JFrame {

    private ClientSocketImpl clientSocket;
    private int port;
    private String serverAddress, username, password;
    private Thread clientThread;
    private DefaultListModel model;
    private File file;
    //TODO
    private String historyFile = "";
    private HistoryFrame historyFrame;
    private History history;

    private JLabel jLHostAddress, jLHostPort, jLName, jLPassword, jLMessage, jLFile,
            jLHistory;
    private JTextField jTHostAddress, jTHostPort, jTUserName, jTMessage, jTFileBrowse, jTHistoryBrowse;
    private JButton jBConnect, jBLogin, jBSignup, jBSendMessage, jBBrowse, jBSend,
            jBBrowse2, jBShow;
    private JPasswordField jPasswordField;
    private JSeparator jSeparator1, jSeparator2;
    private JScrollPane jScrollPaneText, jScrollPaneList;
    private JTextArea jTextArea;
    private JList jList;

    public ClientFrame() {
        initComponents();
        this.setTitle("Messenger");
        model.addElement("All");
        jList.setSelectedIndex(0);
        jTHistoryBrowse.setEnabled(false);
        setDefaultCloseOperation(3);

//        this.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                clientSocket.send(new Message("message", username, ".bye", "SERVER"));
//            }
//        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        EventQueue.invokeLater(() -> new ClientFrame().setVisible(true));
    }

    private void initComponents() {
        //////////////LABELS////////////////
        jLHostAddress = new JLabel("Host Address : ");
        jLHostPort = new JLabel("Host Port : ");
        jLName = new JLabel("Username : ");
        jLPassword = new JLabel("Password : ");
        jLMessage = new JLabel("Message : ");
        jLFile = new JLabel("File : ");
        jLHistory = new JLabel("History File : ");

        //////////////TEXT FIELDS///////////
        jTHostAddress = new JTextField("localhost");
        jTHostPort = new JTextField("1234");
        jTHostPort.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) {
                    e.consume();
                } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) {
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    DataFlavor flavor = DataFlavor.stringFlavor;
                    String schowek = "";

                    try {
                        schowek = (String) clipboard.getData(flavor);
                    } catch (UnsupportedFlavorException | IOException e1) {
                        System.out.println("To nie jest String");
                    }

                    for (int i = 0; i < schowek.length(); i++)
                        if (!Character.isDigit(schowek.charAt(i))) {
                            e.consume();
                            break;
                        }
                }
            }
        });
        jTUserName = new JTextField("Szefo");
        jTUserName.setEnabled(false);
        jTMessage = new JTextField();
        jTFileBrowse = new JTextField("browse2");
        jTHistoryBrowse = new JTextField();

        jPasswordField = new JPasswordField("password");
        jPasswordField.setEnabled(false);

        jTextArea = new JTextArea(5, 20);
        jTextArea.setFont(new Font("Consolas", 0, 12));

        jScrollPaneText = new JScrollPane(jTextArea);

        jList = new JList(model = new DefaultListModel());
        jScrollPaneList = new JScrollPane(jList);

        jSeparator1 = new JSeparator();
        jSeparator2 = new JSeparator();

        setButtons();
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        setLayout(layout);

        pack();
    }

    public void setButtons() {
        jBConnect = new JButton("Connect");
        jBConnect.addActionListener(e -> {
            serverAddress = jTHostAddress.getText().toString();
            port = Integer.parseInt(jTHostPort.getText());

            if (!serverAddress.isEmpty() && !jTHostPort.getText().isEmpty()) {
                try {
                    clientSocket = new ClientSocketImpl(this);
                    clientThread = new Thread(clientSocket);
                    clientThread.start();
                    clientSocket.send(new Message("test", "testUser", "testContent", "SERVER"));
                } catch (IOException e1) {
                    jTextArea.append("Application > Me : Server not found\n");
                }
            }
        });

        jBSignup = new JButton("SignUp");
        jBSignup.setEnabled(false);
        jBSignup.addActionListener(e -> {
            username = jTUserName.getText();
            password = String.valueOf(jPasswordField.getPassword());
            if (!username.isEmpty() && !password.isEmpty()) {
                clientSocket.send(new Message("signup", username, password, "SERVER"));
            }
        });

        jBSendMessage = new JButton("Send Message");
        jBSendMessage.setEnabled(false);
        jBSendMessage.addActionListener(e -> {
            String msg = jTMessage.getText();
            String target = jList.getSelectedValue().toString();
            if (!msg.isEmpty() && !target.isEmpty()) {
                jTMessage.setText("");
                clientSocket.send(new Message("message", username, msg, target));
            }
        });

        jBLogin = new JButton("Login");
        jBLogin.setEnabled(false);
        jBLogin.addActionListener(e -> {
            username = jTUserName.getText();
            password = String.valueOf(jPasswordField.getPassword());
            if (!username.isEmpty() && !password.isEmpty()) {
                clientSocket.send(new Message("login", username, password, "SERVER"));
            }
        });

        jBBrowse = new JButton("...");
        jBBrowse.setEnabled(false);
        jBBrowse.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.showDialog(this, "Select file");

            if (file != null && !file.getName().isEmpty()) {
                jBBrowse.setEnabled(true);
                String string;

                if (jTFileBrowse.getText().length() > 30) {
                    String t = file.getPath();
                    string = t.substring(0, 20) + " [...] " + t.substring(t.length() - 20, t.length());
                } else
                    string = file.getPath();
                jTFileBrowse.setText(string);
            }
        });

        jBSend = new JButton("Send");
        jBSend.setEnabled(false);
        jBSend.addActionListener(e -> {
            long size = file.length();
            if (size < 120 * 1024 * 1024) {
                clientSocket.send(new Message("upload_req", username, file.getName(), jList.getSelectedValue().toString()));
            } else {
                jTextArea.append("[Application > Me] : File is size to large");
            }
        });

        jBBrowse2 = new JButton("...");
        jBBrowse2.setEnabled(false);
        jBBrowse2.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.showDialog(this, "Select file");

            if (!jFileChooser.getSelectedFile().getPath().isEmpty()) {
                historyFile = jFileChooser.getSelectedFile().getPath();

                jTHistoryBrowse.setText(historyFile);
                jTHistoryBrowse.setEnabled(false);
                jBBrowse2.setEnabled(false);
                jBShow.setEnabled(false);
                history = new History(historyFile);

                historyFrame = new HistoryFrame(history);
                historyFrame.setDefaultCloseOperation(HIDE_ON_CLOSE);
                historyFrame.setVisible(false);
            }
        });

        jBShow = new JButton("Show");
        jBShow.setEnabled(false);
        jBShow.addActionListener(e -> {
            historyFrame.setLocation(this.getLocation());
            historyFrame.setVisible(true);
        });
    }

    public void setLayout(GroupLayout layout) {
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jSeparator2)
                                        .addComponent(jSeparator1, GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jLHostAddress)
                                                        .addComponent(jLName)
                                                        .addComponent(jLHistory))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jTUserName)
                                                                        .addComponent(jTHostAddress))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                                        .addComponent(jLHostPort)
                                                                        .addComponent(jLPassword))
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jTHostPort)
                                                                        .addComponent(jPasswordField)))
                                                        .addComponent(jTHistoryBrowse))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jBConnect, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                                        .addComponent(jBLogin, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(jBBrowse2, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(jBShow, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(jBSignup, GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)))))
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jScrollPaneText)
                                                .addGap(18, 18, 18)
                                                .addComponent(jScrollPaneList, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(23, 23, 23)
                                                .addComponent(jLFile)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jTFileBrowse, GroupLayout.PREFERRED_SIZE, 378, GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jBBrowse, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jBSend, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jLMessage)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTMessage)
                                                .addGap(18, 18, 18)
                                                .addComponent(jBSendMessage, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLHostAddress)
                                        .addComponent(jTHostAddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLHostPort)
                                        .addComponent(jTHostPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jBConnect))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTUserName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLPassword)
                                        .addComponent(jLName)
                                        .addComponent(jBSignup)
                                        .addComponent(jPasswordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jBLogin))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLHistory)
                                        .addComponent(jTHistoryBrowse, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jBBrowse2)
                                        .addComponent(jBShow))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPaneText)
                                        .addComponent(jScrollPaneList, GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jBSendMessage)
                                        .addComponent(jLMessage)
                                        .addComponent(jTMessage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator2, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE, false)
                                        .addComponent(jBSend, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jBBrowse, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLFile)
                                        .addComponent(jTFileBrowse, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );
    }

    ////////////GETTERS///////
    public int getPort() {
        return port;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public History getHistory() {
        return history;
    }

    public HistoryFrame getHistoryFrame() {
        return historyFrame;
    }

    public String getUsername() {
        return username;
    }

    public JTextArea getjTextArea() {
        return jTextArea;
    }

    public JButton getjBLogin() {
        return jBLogin;
    }

    public JButton getjBSignup() {
        return jBSignup;
    }

    public JButton getjBSendMessage() {
        return jBSendMessage;
    }

    public JButton getjBBrowse() {
        return jBBrowse;
    }

    public JTextField getjTUserName() {
        return jTUserName;
    }

    public JPasswordField getjPasswordField() {
        return jPasswordField;
    }

    public JButton getjBConnect() {
        return jBConnect;
    }

    public DefaultListModel getModel() {
        return model;
    }

    public Thread getClientThread() {
        return clientThread;
    }

    public File getFile() {
        return file;
    }

    public JTextField getjTHostPort() {
        return jTHostPort;
    }

    public JTextField getjTHostAddress() {
        return jTHostAddress;
    }

    public JButton getjBBrowse2() {
        return jBBrowse2;
    }

    public JButton getjBSend() {
        return jBSend;
    }

    public JTextField getjTFileBrowse() {
        return jTFileBrowse;
    }
}
