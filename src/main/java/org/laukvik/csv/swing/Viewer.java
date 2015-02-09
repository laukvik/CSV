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

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import org.laukvik.csv.CSV;
import org.laukvik.csv.CSVFileFilter;
import org.laukvik.csv.CSVTableModel;
import org.laukvik.csv.InvalidRowDataException;
import org.laukvik.csv.ParseException;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;
import org.laukvik.csv.columns.DateColumn;
import org.laukvik.csv.columns.DoubleColumn;
import org.laukvik.csv.columns.FloatColumn;
import org.laukvik.csv.columns.IntegerColumn;
import org.laukvik.csv.columns.StringColumn;
import org.laukvik.csv.columns.UrlColumn;
import org.laukvik.csv.query.Query;

/**
 *
 * @author morten
 */
public class Viewer extends javax.swing.JFrame implements ListSelectionListener, ActionListener, RecentFileListener {

    private CSV csv = null;
    private File file = null;
    private CSVTableModel model;
    private ResourceBundle bundle;
    private static final Logger LOG = Logger.getLogger(Viewer.class.getName());
    private List<UniqueTableModel> tableModels;

    private RecentFileModel recentFileModel;

    /**
     * Creates new form Viewer
     */
    public Viewer() {
        super("CSV");
        tableModels = new ArrayList<>();
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
        table.setRowSelectionAllowed(true);
        table.setCellSelectionEnabled(true);
        table.setRowHeight(20);


        table.getSelectionModel().addListSelectionListener(this);

        file = null;
        csv = new CSV();
        model = new CSVTableModel(csv);
        table.setModel(model);


        for (Charset c : Charset.availableCharsets().values()) {
            JMenuItem item = new JMenuItem(c.name());
            charsetMenu.add(item);
        }

        /* Recent stuff */
        recentFileModel = new RecentFileModel(recentMenu, this);

        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();

        Float width = size.width * 0.8f;
        Float height = size.height * 0.7f;
        Float split = size.width * 0.2f;
        jSplitPane1.setDividerLocation(split.intValue());

        setSize(width.intValue(), height.intValue());
    }

    public void updateStatus() {
        int min = table.getSelectionModel().getMinSelectionIndex();
        int max = table.getSelectionModel().getMaxSelectionIndex();
        statusLabel.setText(csv.getRowCount() + " rows");
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        updateStatus();

    }

    public CSVTableModel getModel() {
        return model;
    }

    public void buildQuery() {
        LOG.info("buildQuery ");
        Query.Where where = csv.findByQuery().where();
        int selectionCount = 0;
        for (UniqueTableModel utm : tableModels) {
            Column c = utm.getColumn();

            if (c instanceof IntegerColumn) {

                IntegerColumn ic = (IntegerColumn) c;
                UniqueTableModel<Integer> mod = (UniqueTableModel<Integer>) utm;

                Integer[] arr = new Integer[utm.getSelection().size()];
                int x = 0;
                for (Integer v : mod.getSelection()) {
                    arr[x] = v;
                    x++;
                }

                if (arr.length > 0) {
                    where.column(c.getName()).isIn(arr);
                }
                selectionCount += arr.length;

            } else if (c instanceof FloatColumn) {

                FloatColumn ic = (FloatColumn) c;
                UniqueTableModel<Float> mod = (UniqueTableModel<Float>) utm;

                Float[] arr = new Float[utm.getSelection().size()];
                int x = 0;
                for (Float v : mod.getSelection()) {
                    arr[x] = v;
                    x++;
                }

                if (arr.length > 0) {
                    where.column(c.getName()).isIn(arr);
                }
                selectionCount += arr.length;

            } else if (c instanceof DoubleColumn) {
                DoubleColumn ic = (DoubleColumn) c;
                UniqueTableModel<Double> mod = (UniqueTableModel<Double>) utm;

                Double[] arr = new Double[utm.getSelection().size()];
                int x = 0;
                for (Double v : mod.getSelection()) {
                    arr[x] = v;
                    x++;
                }

                if (arr.length > 0) {
                    where.column(c.getName()).isIn(arr);
                }
                selectionCount += arr.length;

            } else if (c instanceof DateColumn) {
                DateColumn ic = (DateColumn) c;
                UniqueTableModel<Date> mod = (UniqueTableModel<Date>) utm;

                Date[] arr = new Date[utm.getSelection().size()];
                int x = 0;
                for (Date v : mod.getSelection()) {
                    arr[x] = v;
                    x++;
                }

                if (arr.length > 0) {
                    where.column(c.getName()).isIn(arr);
                }
                selectionCount += arr.length;
            } else {
                StringColumn ic = (StringColumn) c;
                UniqueTableModel<String> mod = (UniqueTableModel<String>) utm;
                String[] arr = new String[utm.getSelection().size()];
                int x = 0;
                for (String v : mod.getSelection()) {
                    arr[x] = v;
                    x++;
                }

                if (arr.length > 0) {
                    where.column(c.getName()).isIn(arr);

                }
                selectionCount += arr.length;
            }

        }
        if (selectionCount == 0) {
            model = new CSVTableModel(csv);
        } else {
            model = new CSVTableModel(where.getResultList(), csv.getMetaData());
        }


        table.setModel(model);
        table.tableChanged(new TableModelEvent(model));
    }

    public void createUniqueModels() {
        LOG.info("Adding unique models: " + csv.getMetaData().getColumnCount());
        jTabbedPane1.removeAll();
        tableModels = new ArrayList<>();

        for (int x = 0; x < csv.getMetaData().getColumnCount(); x++) {
            Column c = csv.getMetaData().getColumn(x);

            /**/
            UniqueTableModel model = null;
            if (c instanceof IntegerColumn) {
                model = new UniqueTableModel<Integer>(c);
                for (int y = 0; y < csv.getRowCount(); y++) {
                    Row r = csv.getRow(y);
                    model.addValue(r.getInteger(x));
                }
            } else if (c instanceof FloatColumn) {
                model = new UniqueTableModel<Float>(c);
                for (int y = 0; y < csv.getRowCount(); y++) {
                    Row r = csv.getRow(y);
                    model.addValue(r.getFloat(x));
                }
            } else if (c instanceof IntegerColumn) {
                model = new UniqueTableModel<Integer>(c);
                for (int y = 0; y < csv.getRowCount(); y++) {
                    Row r = csv.getRow(y);
                    model.addValue(r.getInteger(x));
                }
            } else if (c instanceof DateColumn) {
                model = new UniqueTableModel<Date>(c);
                for (int y = 0; y < csv.getRowCount(); y++) {
                    Row r = csv.getRow(y);
                    model.addValue(r.getValue(x));
                }
            } else if (c instanceof UrlColumn) {
                model = new UniqueTableModel(c);
                for (int y = 0; y < csv.getRowCount(); y++) {
                    Row r = csv.getRow(y);
                    model.addValue(r.getValue(x));
                }
            } else {
                model = new UniqueTableModel<String>(c);
                for (int y = 0; y < csv.getRowCount(); y++) {
                    Row r = csv.getRow(y);
                    model.addValue(r.getString(x));
                }
            }


            /* Build the current values */
            model.buildValues();

            LOG.info("Adding unique for column " + c.getName());

            tableModels.add(model);

            model.addChangeListener(new UniqueListener() {
                @Override
                public void uniqueSelectionChanged(UniqueTableModel model) {
                    LOG.info("Selection: " + model);
                    buildQuery();
                }
            });

            JTable table = new JTable(model);
            table.setRowHeight(20);
            table.setCellSelectionEnabled(false);
            table.setRowSelectionAllowed(false);

            table.getColumnModel().getColumn(0).setMinWidth(32);
            table.getColumnModel().getColumn(0).setMaxWidth(32);
            table.getColumnModel().getColumn(0).setPreferredWidth(32);
            table.getColumnModel().getColumn(0).setWidth(32);

            table.getColumnModel().getColumn(2).setMinWidth(32);
            table.getColumnModel().getColumn(2).setMaxWidth(100);
            table.getColumnModel().getColumn(2).setPreferredWidth(64);


            table.setVisible(true);
            JScrollPane scroll = new JScrollPane(table);
            scroll.setVisible(true);
            jTabbedPane1.add(c.getName(), scroll);
        }
        jTabbedPane1.invalidate();
    }

//    public void openCSV(CSV csv) {
//        model = new CSVTableModel(csv);
//        table.setModel(model);
//        setTitle("Untitled");
//    }

    public void openFile(File file)  {

        try {
            csv = new CSV(file);
            this.file = file;
            model = new CSVTableModel(csv);
            table.setModel(model);
            setTitle(file.getAbsolutePath());
            statusLabel.setText(csv.getRowCount() + " rows");
            getRootPane().putClientProperty("Window.documentFile", file);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            for (int x = 0; x < csv.getMetaData().getColumnCount(); x++) {
                table.getColumnModel().getColumn(x).setWidth(150);
            }
            /* */
            recentFileModel.add(new RecentFile(file.getAbsolutePath()));
            createUniqueModels();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Fant ikke fil", "", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Kunne ikke åpne", "", JOptionPane.ERROR_MESSAGE);
        } catch (InvalidRowDataException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage() + "\n" + ex.getRow().getRaw(), "Feil i CSV fil", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage() + "\n", "Feil i CSV fil", JOptionPane.ERROR_MESSAGE);
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

        jSplitPane1 = new javax.swing.JSplitPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jToolBar2 = new javax.swing.JToolBar();
        statusLabel = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        openMenuItem = new javax.swing.JMenuItem();
        recentMenu = new javax.swing.JMenu();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        printMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        cutMenuItem = new javax.swing.JMenuItem();
        copyMenuItem = new javax.swing.JMenuItem();
        pasteMenuItem = new javax.swing.JMenuItem();
        deleteRowMenuItem = new javax.swing.JMenuItem();
        insertRowMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        gotoMenuItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        insertColumnMenuItem = new javax.swing.JMenuItem();
        deleteColumnMenuItem = new javax.swing.JMenuItem();
        charsetMenu = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jSplitPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jSplitPane1.setDividerLocation(250);
        jSplitPane1.setOneTouchExpandable(true);
        jSplitPane1.setLeftComponent(jTabbedPane1);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

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

        jSplitPane1.setRightComponent(jScrollPane1);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

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
        fileMenu.add(jSeparator4);

        openMenuItem.setText(bundle.getString("open")); // NOI18N
        openMenuItem.setToolTipText("Opens a CSV file for editing");
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openMenuItem);

        recentMenu.setText("Open recent");
        fileMenu.add(recentMenu);

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

        jMenuItem1.setText("Undo");
        editMenu.add(jMenuItem1);

        jMenuItem2.setText("Redo");
        editMenu.add(jMenuItem2);
        editMenu.add(jSeparator5);

        cutMenuItem.setText(bundle.getString("cut")); // NOI18N
        editMenu.add(cutMenuItem);

        copyMenuItem.setText(bundle.getString("copy")); // NOI18N
        editMenu.add(copyMenuItem);

        pasteMenuItem.setText(bundle.getString("paste")); // NOI18N
        editMenu.add(pasteMenuItem);

        deleteRowMenuItem.setText(bundle.getString("row_delete")); // NOI18N
        deleteRowMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteRowMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(deleteRowMenuItem);

        insertRowMenuItem.setText("Insert row");
        insertRowMenuItem.setToolTipText("");
        insertRowMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertRowMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(insertRowMenuItem);
        editMenu.add(jSeparator2);

        gotoMenuItem.setText("Goto...");
        gotoMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gotoMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(gotoMenuItem);
        editMenu.add(jSeparator3);
        editMenu.add(jSeparator6);

        jMenuItem3.setText("Find");
        editMenu.add(jMenuItem3);

        jMenuItem6.setText(bundle.getString("replace")); // NOI18N
        editMenu.add(jMenuItem6);

        jMenuBar1.add(editMenu);

        toolsMenu.setText(bundle.getString("tools")); // NOI18N

        insertColumnMenuItem.setText(bundle.getString("column_new")); // NOI18N
        insertColumnMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertColumnMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(insertColumnMenuItem);

        deleteColumnMenuItem.setText(bundle.getString("column_delete")); // NOI18N
        deleteColumnMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteColumnMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(deleteColumnMenuItem);

        charsetMenu.setText("Encoding");

        jMenuItem4.setText("UTF-8");
        charsetMenu.add(jMenuItem4);

        jMenuItem5.setText("ISO-8859-1");
        charsetMenu.add(jMenuItem5);

        toolsMenu.add(charsetMenu);

        jMenuBar1.add(toolsMenu);

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
            openFile(new File(fd.getDirectory(), filename));

//            try {
//                openFile(new File(fd.getDirectory(), filename));
//            } catch (FileNotFoundException ex) {
//                JOptionPane.showMessageDialog(this, "Fant ikke fil", "", JOptionPane.ERROR_MESSAGE);
//            } catch (IOException ex) {
//                JOptionPane.showMessageDialog(this, "Kunne ikke åpne", "", JOptionPane.ERROR_MESSAGE);
//            } catch (InvalidRowDataException ex) {
//                JOptionPane.showMessageDialog(this, ex.getMessage() + "\n" + ex.getRow().getRaw() , "Feil i CSV fil", JOptionPane.ERROR_MESSAGE);
//            } catch (ParseException ex) {
//                JOptionPane.showMessageDialog(this, ex.getMessage() + "\n", "Feil i CSV fil", JOptionPane.ERROR_MESSAGE);
//            }
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
        int min = table.getSelectedRow();
        int max = min + table.getSelectedRowCount();
        csv.removeRows(min,max);
        table.tableChanged(new TableModelEvent(model));
    }//GEN-LAST:event_deleteRowMenuItemActionPerformed

    private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuItemActionPerformed
        file = null;
        csv = new CSV();
        model = new CSVTableModel(csv);
        table.setModel(model);
        jTabbedPane1.removeAll();
        tableModels.clear();

        getRootPane().putClientProperty("Window.documentFile", null);
        setTitle("");
    }//GEN-LAST:event_newMenuItemActionPerformed

    private void insertColumnMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertColumnMenuItemActionPerformed

        String answer = JOptionPane.showInputDialog(this,"", "", JOptionPane.QUESTION_MESSAGE);
        if (answer!=null){
            int rowIndex = table.getSelectedColumn();
            if (rowIndex == -1) {
                LOG.info("Inserting column after last");
                csv.addColumn(answer);
            } else {
                LOG.info("Inserting column at " + rowIndex);
                csv.insertColumn(answer, rowIndex);
            }
            LOG.info("Columns: " + csv.getMetaData().getColumnCount());

            table.tableChanged(new TableModelEvent(model, TableModelEvent.HEADER_ROW));
            table.tableChanged(new TableModelEvent(model));
        }

    }//GEN-LAST:event_insertColumnMenuItemActionPerformed

    private void insertRowMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertRowMenuItemActionPerformed
        int rowIndex = table.getSelectedRow();

        Row emptyRow = csv.getMetaData().createEmptyRow();

        if (rowIndex == -1) {
            LOG.info("Adding empty row at end");
            csv.addRow(emptyRow);
        } else {
            LOG.info("Inserting row at " + rowIndex);
            csv.insertRow(emptyRow, rowIndex);
        }

        LOG.info("Rows after insert: " + csv.getRowCount());

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
        if (min > -1){
        }
        csv.removeColumn(min);
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

            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenu charsetMenu;
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
    private javax.swing.JMenuItem insertRowMenuItem;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenuItem pasteMenuItem;
    private javax.swing.JMenuItem printMenuItem;
    private javax.swing.JMenu recentMenu;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JTable table;
    private javax.swing.JMenu toolsMenu;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
