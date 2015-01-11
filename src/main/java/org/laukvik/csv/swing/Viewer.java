/*
 * Copyright 2013 Laukviks Bedrifter.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laukvik.csv.swing;

import java.awt.FileDialog;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import org.laukvik.csv.CSV;
import org.laukvik.csv.CSVFileFilter;
import org.laukvik.csv.CSVTableModel;
import org.laukvik.csv.Row;

/**
 *
 * @author morten
 */
public class Viewer extends javax.swing.JFrame implements ListSelectionListener {

    private CSV csv = null;
    private File file = null;
    private CSVTableModel model;
    private ResourceBundle bundle;

    /**
     * Creates new form Viewer
     */
    public Viewer() {
        super("CSV");
        bundle = ResourceBundle.getBundle("org.laukvik.csv.messages");
        setTitle(bundle.getString("app"));
        setTitle("");
        initComponents();

        newMenuItem.setAccelerator(getKeystroke(java.awt.event.KeyEvent.VK_N));
        fileMenu.setText(bundle.getString("file"));
        openMenuItem.setText(bundle.getString("open"));
        openMenuItem.setAccelerator(getKeystroke(java.awt.event.KeyEvent.VK_O));
        saveMenuItem.setText(bundle.getString("save"));
        saveMenuItem.setAccelerator(getKeystroke(java.awt.event.KeyEvent.VK_S));
        saveAsMenuItem.setText(bundle.getString("saveas"));
        printMenuItem.setAccelerator(getKeystroke(java.awt.event.KeyEvent.VK_P));
        exitMenuItem.setText(bundle.getString("exit"));
        exitMenuItem.setAccelerator(getKeystroke(java.awt.event.KeyEvent.VK_Q));

        gotoMenuItem.setAccelerator(getKeystroke(java.awt.event.KeyEvent.VK_G));

        helpMenu.setText(bundle.getString("help"));
        aboutMenuItem.setText(bundle.getString("about"));

        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(false);

        table.getSelectionModel().addListSelectionListener(this);

        file = null;
        csv = new CSV();
        model = new CSVTableModel(csv);
        table.setModel(model);
        jScrollPane1.setVisible(false);

    }

    public void updateStatus() {
        int min = table.getSelectionModel().getMinSelectionIndex();
        int max = table.getSelectionModel().getMaxSelectionIndex();
//        String text = csv.getRowCount() + "x" + csv.getMetaData().getColumnCount() + " " + min + " - " + max;
//        statusLabel.setText(text);
        statusLabel.setText(csv.getRowCount() + " rows");
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        updateStatus();

    }

    public CSVTableModel getModel() {
        return model;
    }


    public void openCSV(CSV csv) {
        model = new CSVTableModel(csv);
        table.setModel(model);
        setTitle("Untitled");
    }

    public void openFile(File file) throws FileNotFoundException, IOException {
        this.file = file;
        csv = new CSV(file);
        model = new CSVTableModel(csv);
        table.setModel(model);
        setTitle(file.getAbsolutePath());
        statusLabel.setText(csv.getRowCount() + " rows");
        getRootPane().putClientProperty("Window.documentFile", file);

        jScrollPane1.setVisible(true);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int x = 0; x < csv.getMetaData().getColumnCount(); x++) {
            table.getColumnModel().getColumn(x).setWidth(150);
        }

    }

    /**
     * Returns a cross-platform keystroke that enables the platform behave
     * natively. This is usually a problem for Mac people who uses the Apple
     * button like Windows people use the Control button.
     *
     * @param keyevent the keyevent you want the keystroke for
     * @return a cross-platfrom compatible keystroke
     */
    public static KeyStroke getKeystroke(int keyevent) {
        return KeyStroke.getKeyStroke(keyevent, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jToolBar2 = new javax.swing.JToolBar();
        statusLabel = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        printMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        cutMenuItem = new javax.swing.JMenuItem();
        copyMenuItem = new javax.swing.JMenuItem();
        pasteMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        gotoMenuItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        insertMenu = new javax.swing.JMenu();
        insertRowMenuItem = new javax.swing.JMenuItem();
        deleteRowMenuItem = new javax.swing.JMenuItem();
        insertColumnMenuItem = new javax.swing.JMenuItem();
        deleteColumnMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        table.setCellSelectionEnabled(true);
        table.setShowGrid(true);
        jScrollPane1.setViewportView(table);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        statusLabel.setText("0 rows");
        jToolBar2.add(statusLabel);

        getContentPane().add(jToolBar2, java.awt.BorderLayout.PAGE_END);

        fileMenu.setText("File");

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/laukvik/csv/messages"); // NOI18N
        newMenuItem.setText(bundle.getString("new")); // NOI18N
        newMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(newMenuItem);

        openMenuItem.setText(bundle.getString("open")); // NOI18N
        openMenuItem.setToolTipText("Opens a CSV file for editing");
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openMenuItem);

        saveMenuItem.setText("Save");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setText("Save as...");
        saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveAsMenuItem);

        printMenuItem.setText(bundle.getString("print")); // NOI18N
        fileMenu.add(printMenuItem);
        fileMenu.add(jSeparator1);

        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        jMenuBar1.add(fileMenu);

        editMenu.setText(bundle.getString("edit")); // NOI18N

        cutMenuItem.setText(bundle.getString("cut")); // NOI18N
        editMenu.add(cutMenuItem);

        copyMenuItem.setText(bundle.getString("copy")); // NOI18N
        editMenu.add(copyMenuItem);

        pasteMenuItem.setText(bundle.getString("paste")); // NOI18N
        editMenu.add(pasteMenuItem);
        editMenu.add(jSeparator2);

        gotoMenuItem.setText("Goto...");
        gotoMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gotoMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(gotoMenuItem);
        editMenu.add(jSeparator3);

        jMenuBar1.add(editMenu);

        insertMenu.setText("jMenu2");

        insertRowMenuItem.setText("Insert row");
        insertRowMenuItem.setToolTipText("");
        insertRowMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertRowMenuItemActionPerformed(evt);
            }
        });
        insertMenu.add(insertRowMenuItem);

        deleteRowMenuItem.setText("Delete row");
        deleteRowMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteRowMenuItemActionPerformed(evt);
            }
        });
        insertMenu.add(deleteRowMenuItem);

        insertColumnMenuItem.setText("Insert column");
        insertColumnMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertColumnMenuItemActionPerformed(evt);
            }
        });
        insertMenu.add(insertColumnMenuItem);

        deleteColumnMenuItem.setText("Delete column");
        deleteColumnMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteColumnMenuItemActionPerformed(evt);
            }
        });
        insertMenu.add(deleteColumnMenuItem);

        jMenuBar1.add(insertMenu);

        helpMenu.setText("Help");

        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        jMenuBar1.add(helpMenu);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed

        java.awt.FileDialog fd = new FileDialog(this,"Velg fil", FileDialog.LOAD);
        fd.setFilenameFilter(new CSVFileFilter());
        fd.setVisible(true);
        String filename = fd.getFile();
        if (filename == null) {
        } else {
            try {
                openFile(new File(fd.getDirectory(), filename));
            } catch (IOException ex) {
                Logger.getLogger(Viewer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_openMenuItemActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed

        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuItemActionPerformed


        java.awt.FileDialog fd = new FileDialog(this,"Velg fil", FileDialog.SAVE);
        fd.setFilenameFilter(new CSVFileFilter());
        fd.setVisible(true);

        String filename = fd.getFile();
        if (filename == null) {
        } else {
            try {
                File file = new File(fd.getDirectory(), filename);
                csv.write(file);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Could not save file!", "", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_saveAsMenuItemActionPerformed

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        AboutDialog d = new AboutDialog(this, true);
        d.setLocationRelativeTo(null);
        d.setVisible(true);
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
            try {
                csv.write(file);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Could not save file!", "", JOptionPane.WARNING_MESSAGE);
            }
    }//GEN-LAST:event_saveMenuItemActionPerformed

    private void deleteRowMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteRowMenuItemActionPerformed
        int min = table.getSelectionModel().getMinSelectionIndex();
        int max = table.getSelectionModel().getMaxSelectionIndex();
        csv.removeRows(min,max);
        table.tableChanged(new TableModelEvent(model));
    }//GEN-LAST:event_deleteRowMenuItemActionPerformed

    private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuItemActionPerformed
        file = null;
        csv = new CSV();
        model = new CSVTableModel(csv);
        table.setModel(model);
        jScrollPane1.setVisible(false);
        getRootPane().putClientProperty("Window.documentFile", null);
        setTitle("");
    }//GEN-LAST:event_newMenuItemActionPerformed

    private void insertColumnMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertColumnMenuItemActionPerformed

        String answer = JOptionPane.showInputDialog(this,"Navn", "", JOptionPane.QUESTION_MESSAGE);
        if (answer!=null){
            int min = table.getSelectionModel().getMinSelectionIndex();
            csv.insertColumn(answer, min);
            table.tableChanged(new TableModelEvent(model, TableModelEvent.HEADER_ROW));
            jScrollPane1.setVisible(true);
        }

    }//GEN-LAST:event_insertColumnMenuItemActionPerformed

    private void insertRowMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertRowMenuItemActionPerformed
        int min = table.getSelectionModel().getMinSelectionIndex();

        List<String> values = new ArrayList<>();
        for (int x=0; x<csv.getMetaData().getColumnCount(); x++){
            values.add("");
        }

        csv.insertRow(new Row(values), min);

        table.tableChanged( new TableModelEvent(model));

    }//GEN-LAST:event_insertRowMenuItemActionPerformed

    private void gotoMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gotoMenuItemActionPerformed
        String answer = JOptionPane.showInputDialog(this,"Linje", "", JOptionPane.QUESTION_MESSAGE);
        if (csv.getRowCount() == 0){
            return;
        }
        if (answer!=null){
            Integer row = Integer.parseInt( answer) -1;
            table.setRowSelectionInterval(row, row);
            Rectangle rect = table.getCellRect(row, 0, true);
            rect.height = (int) jScrollPane1.getVisibleRect().getHeight();
            table.scrollRectToVisible(rect);
        }
    }//GEN-LAST:event_gotoMenuItemActionPerformed

    private void deleteColumnMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteColumnMenuItemActionPerformed

        int min = table.getSelectedRow();
        csv.removeColumn(0);
        table.tableChanged( new TableModelEvent(model,TableModelEvent.HEADER_ROW));
    }//GEN-LAST:event_deleteColumnMenuItemActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Viewer v = new Viewer();
                v.setSize(700, 400);
                v.setLocationRelativeTo(null);
                v.setVisible(true);
//                try {
//                    v.openFile(new File("/Users/morten/Desktop/cars.csv"));
//                } catch (IOException ex) {
//                    Logger.getLogger(Viewer.class.getName()).log(Level.SEVERE, null, ex);
//                }

            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuItem copyMenuItem;
    private javax.swing.JMenuItem cutMenuItem;
    private javax.swing.JMenuItem deleteColumnMenuItem;
    private javax.swing.JMenuItem deleteRowMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem gotoMenuItem;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem insertColumnMenuItem;
    private javax.swing.JMenu insertMenu;
    private javax.swing.JMenuItem insertRowMenuItem;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenuItem pasteMenuItem;
    private javax.swing.JMenuItem printMenuItem;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables


}