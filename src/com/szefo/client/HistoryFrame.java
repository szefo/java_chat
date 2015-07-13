package com.szefo.client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


public class HistoryFrame extends JFrame {

    private JTable jTable;
    private History history;
    private JLabel jLabel;
    private JScrollPane jScrollPane;

    public HistoryFrame() {
        initComponents();
    }

    public HistoryFrame(History history) {
        initComponents();
        this.history = history;
        history.fillTable(this);
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

        EventQueue.invokeLater(() -> new HistoryFrame().setVisible(true));
    }

    public void initComponents() {
        setDefaultCloseOperation(3);
        setTitle("Chat history");

        jLabel = new JLabel("History : ");

        jTable = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{
                        "Sender", "Message", "To", "Time"
                }
        ) {
            Class[] types = new Class[]{String.class, String.class, String.class, String.class};
            boolean[] canEdit = new boolean[]{false, false, false, false};

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        jScrollPane = new JScrollPane(jTable);
        jTable.getColumnModel().getColumn(1).setPreferredWidth(200);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addComponent(jScrollPane, GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE))
                                .addContainerGap()));

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane, GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
                                .addContainerGap()));

        pack();

    }

    public JTable getjTable() {
        return jTable;
    }
}





























