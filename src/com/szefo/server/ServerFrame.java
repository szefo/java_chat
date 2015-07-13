package com.szefo.server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class ServerFrame extends JFrame {

    private String filePath = "/home/szefo/Documents/projekty/JavaChat/Data.xml";

    private JButton jBstart, jBbrowse;
    private JScrollPane jScrollPane;
    private JTextArea jTextArea;
    private JTextField jTextField;
    private JLabel jLabel;
    private JFileChooser jFileChooser;
    private ServerSocketImpl serverSocketImpl;

    public ServerFrame() {
        setDefaultCloseOperation(3);
        setTitle("Server");
        initComponents();
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

        EventQueue.invokeLater(() -> new ServerFrame().setVisible(true));
    }

    public JTextArea getjTextArea() {
        return jTextArea;
    }

    public String getFilePath() {
        return filePath;
    }

    private void initComponents() {
        jFileChooser = new JFileChooser();

        jTextField = new JTextField();
        jTextField.setEditable(false);

        jBstart = new JButton("Start Server");
        jBstart.setEnabled(false);
        jBstart.addActionListener(e -> {
            jbStart(e);
        });

        jLabel = new JLabel("Database file: ");

        jBbrowse = new JButton("Browse");
        jBbrowse.addActionListener(e -> {
            jbBrowse(e);
        });

        jTextArea = new JTextArea(20, 5);
        jTextArea.setEditable(false);
        jScrollPane = new JScrollPane(jTextArea);

        setLayout();
    }

    private void setLayout(){
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane)
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jTextField, GroupLayout.PREFERRED_SIZE, 280, Short.MAX_VALUE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jBbrowse, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jBstart, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel)
                                        .addComponent(jBbrowse)
                                        .addComponent(jBstart))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane, GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                                .addContainerGap()));

        pack();
    }

    private void jbStart(ActionEvent e) {
        serverSocketImpl = new ServerSocketImpl(this);
        jBstart.setEnabled(false);
        jBbrowse.setEnabled(false);
    }

    private void jbBrowse(ActionEvent e) {
        jFileChooser.showDialog(this, "Select");
        File file = jFileChooser.getSelectedFile();

        if (file != null) {
            filePath = file.getPath();
            jTextField.setText(filePath);
            jBstart.setEnabled(true);
        }
    }

    public void retryStart(int port) {
        if (serverSocketImpl != null) {
            serverSocketImpl.stop();
            serverSocketImpl = new ServerSocketImpl(this, port);
        }
    }
}
