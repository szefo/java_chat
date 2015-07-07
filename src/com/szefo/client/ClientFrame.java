package com.szefo.client;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ClientFrame extends JFrame {

    private ClientSocketImpl clientSocket;
    private int port;
    private String serverAddress, username, password;
    private Thread clientThread;
    private DefaultListModel model;
    private File file;
    //TODO
    private String historyFile = "";
    //private HistoryFrame historyFrame;
    //private Hisory hisory;

    private JLabel jLHostAddress, jLHostPort, jLName, jLPassword, jLMessage, jLFile,
            jLHistory;
    private JTextField jTHostAddress, jTHostPort, jTAdmin, jTextField4, jTextField5, jTextField6;
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
        jTextField6.setEnabled(false);
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
        jTHostPort = new JTextField("13000");
        jTAdmin = new JTextField("Szefo");
        jTAdmin.setEnabled(false);
        jTextField4 = new JTextField();
        jTextField5 = new JTextField();
        jTextField6 = new JTextField();

        jPasswordField = new JPasswordField("password");
        jPasswordField.setEnabled(false);

        jTextArea = new JTextArea(5, 20);
        jTextArea.setFont(new Font("Consolas", 0, 12));

        jScrollPaneText = new JScrollPane(jTextArea);

        jList = new JList(model = new DefaultListModel());
        jScrollPaneList = new JScrollPane(jList);

        ////////////BUTTONS/////////////////
        jBConnect = new JButton("Connect");
        jBConnect.addActionListener(e -> {

        });

        jBSignup = new JButton("SignUp");
        jBSignup.setEnabled(false);
        jBSignup.addActionListener(e -> {

        });

        jBSendMessage = new JButton("Send Message");
        jBSendMessage.setEnabled(false);
        jBSendMessage.addActionListener(e -> {

        });

        jBLogin = new JButton("Login");
        jBLogin.setEnabled(false);
        jBLogin.addActionListener(e -> {

        });

        jBBrowse = new JButton("...");
        jBBrowse.setEnabled(false);
        jBBrowse.addActionListener(e -> {

        });

        jBSend = new JButton("Send");
        jBSend.setEnabled(false);
        jBSend.addActionListener(e -> {

        });

        jBBrowse2 = new JButton("...");
        jBBrowse2.setEnabled(false);
        jBBrowse2.addActionListener(e -> {

        });

        jBShow = new JButton("Show");
        jBShow.setEnabled(false);
        jBShow.addActionListener(e -> {

        });

        jSeparator1 = new JSeparator();
        jSeparator2 = new JSeparator();


        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
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
                                                                        .addComponent(jTAdmin)
                                                                        .addComponent(jTHostAddress))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                                        .addComponent(jLHostPort)
                                                                        .addComponent(jLPassword))
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jTHostPort)
                                                                        .addComponent(jPasswordField)))
                                                        .addComponent(jTextField6))
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
                                                .addComponent(jTextField5, GroupLayout.PREFERRED_SIZE, 378, GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jBBrowse, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jBSend, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jLMessage)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextField4)
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
                                        .addComponent(jTAdmin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLPassword)
                                        .addComponent(jLName)
                                        .addComponent(jBSignup)
                                        .addComponent(jPasswordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jBLogin))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLHistory)
                                        .addComponent(jTextField6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
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
                                        .addComponent(jTextField4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator2, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE, false)
                                        .addComponent(jBSend, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jBBrowse, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLFile)
                                        .addComponent(jTextField5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        pack();
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

}
