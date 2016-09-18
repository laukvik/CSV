/*
 * Copyright 2015 Laukviks Bedrifter.
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

import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.IllegalColumnDefinitionException;
import org.laukvik.csv.io.CsvReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Morten Laukvik
 */
public class LoadingWorker extends javax.swing.JDialog implements ActionListener {

    private static final Logger LOG = Logger.getLogger(LoadingWorker.class.getName());

    private final Viewer viewer;
    private boolean canContinue;
    ResourceBundle bundle;

    class Task extends SwingWorker<Void, Void> {

        File file;
        Viewer viewer;
        Charset charset;

        public Task(File file, Viewer viewer) {
            this.file = file;
            this.viewer = viewer;
        }

        @Override
        protected Void doInBackground() throws Exception {
            int fileSize = (int) file.length();
            int kb = (int) (fileSize / 1024);
            Dimension size = new Dimension(400, 100);
            setSize(size);
            setPreferredSize(size);
            setMinimumSize(size);
            setMaximumSize(size);
            setLocationRelativeTo(null);
            setUndecorated(true);
            setVisible(true);
            setTitle(bundle.getString("loading"));
            LOG.fine("Starting reading in background...");
            canContinue = true;
            progressBar.setIndeterminate(false);
            progressBar.setMaximum(fileSize);
            progressBar.setStringPainted(false);

            LOG.fine("Setting visible");
            labelFilename.setText(file.getName() + " " + kb + " Kb");
            MessageFormat mf = new MessageFormat(bundle.getString("loading.rows"));
            CSV csv = new CSV();
            boolean success = false;
            try (CsvReader r = new CsvReader(file, charset, null)) {
                csv.setMetaData(r.getMetaData());
                while (canContinue && r.hasNext()) {
                    progressBar.setValue(r.getBytesRead());
                    Row row = r.next();
                    csv.addRow(row);
                    Object[] params = {r.getLineCounter()};
                    label.setText(mf.format(params));
                    LOG.log(Level.FINE, "Reading row {0}", r.getLineCounter());
                }
                success = true;
                setVisible(false);
            }
            catch (IllegalColumnDefinitionException ex) {
                ex.printStackTrace();
                MessageFormat f = new MessageFormat(bundle.getString("loading.column_exception"));
                Object[] params = {file.getAbsolutePath()};
                JOptionPane.showMessageDialog(viewer, f.format(params), "", JOptionPane.ERROR_MESSAGE);
            }
            catch (FileNotFoundException ex) {
                ex.printStackTrace();
                MessageFormat f = new MessageFormat(bundle.getString("loading.file_not_found"));
                Object[] params = {file.getAbsolutePath()};
                JOptionPane.showMessageDialog(viewer, f.format(params), "", JOptionPane.ERROR_MESSAGE);
            }
            catch (IOException ex) {
                ex.printStackTrace();
                MessageFormat f = new MessageFormat(bundle.getString("loading.failed_to_load"));
                Object[] params = {file.getAbsolutePath()};
                JOptionPane.showMessageDialog(viewer, f.format(params), "", JOptionPane.ERROR_MESSAGE);
            }

            LOG.fine("Done reading");
            dispose();
            if (success) {
                viewer.openCSV(csv, file);
            }
            return null;
        }

        @Override
        public void done() {
            LOG.fine("Done!");
        }

    }

    /**
     * Creates new form ProgressDialog
     *
     * @param viewer
     * @param bundle
     */
    public LoadingWorker(Viewer viewer, ResourceBundle bundle) {
        super(viewer);
        this.bundle = bundle;
        this.viewer = viewer;
        initComponents();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LOG.fine("ActionPerformed");

        Charset charset = null;
        try {
            // Reload current file if actioncommand with encoding is set
            charset = Charset.forName(e.getActionCommand());
        }
        catch (Exception e2) {
        }

        if (charset == null) {
            java.awt.FileDialog fd = new FileDialog(viewer, "Velg fil", FileDialog.LOAD);
            fd.setFilenameFilter(new org.laukvik.csv.swing.CSVFileFilter());
            fd.setVisible(true);
            String filename = fd.getFile();
            if (filename == null) {
            } else {
                File file = new File(fd.getDirectory(), filename);
                openFile(file, null);
            }
        } else {
            openFile(viewer.getFile(), charset);
        }

    }

    public void openFile(File file, Charset charset) {
        LOG.log(Level.FINE, "Opening file {0} with charset {1}", new Object[]{file.getAbsolutePath(), charset});
        final Task task = new Task(file, viewer);
        task.charset = charset;
        task.execute();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        progressBar = new javax.swing.JProgressBar();
        button = new javax.swing.JButton();
        label = new javax.swing.JLabel();
        labelFilename = new javax.swing.JLabel();

        setModal(false);
        setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        setSize(new java.awt.Dimension(300, 150));
        getContentPane().setLayout(null);

        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        getContentPane().add(progressBar);
        progressBar.setBounds(20, 20, 360, 30);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("messages"); // NOI18N
        button.setText(bundle.getString("cancel")); // NOI18N
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonActionPerformed(evt);
            }
        });
        getContentPane().add(button);
        button.setBounds(150, 70, 97, 29);

        label.setText("Row");
        getContentPane().add(label);
        label.setBounds(20, 50, 360, 16);

        labelFilename.setText("File");
        labelFilename.setEnabled(false);
        getContentPane().add(labelFilename);
        labelFilename.setBounds(20, 10, 360, 16);
    }// </editor-fold>//GEN-END:initComponents

    private void buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonActionPerformed
        // TODO add your handling code here:
        canContinue = false;
    }//GEN-LAST:event_buttonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button;
    private javax.swing.JLabel label;
    private javax.swing.JLabel labelFilename;
    private javax.swing.JProgressBar progressBar;
    // End of variables declaration//GEN-END:variables

}
