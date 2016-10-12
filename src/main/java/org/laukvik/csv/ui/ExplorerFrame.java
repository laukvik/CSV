package org.laukvik.csv.ui;

import org.laukvik.csv.CSV;
import org.laukvik.csv.ChangeListener;
import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.io.File;


/**
 *
 *
 * @author Morten Laukvik
 */
public class ExplorerFrame extends JFrame implements ChangeListener{

    private ExplorerPanel explorer;
    private ColumnsModel columnsModel;
    private ResultsModel resultsModel;
    private UniqueModel uniqueModel;
    private CSV csv;
    private int selectedColumnIndex;

    public ExplorerFrame(CSV csv){
        super();
        this.csv = csv;
        explorer = new ExplorerPanel();
        setLayout( new BorderLayout() );
        setSize(1600, 700);
        add(explorer, BorderLayout.CENTER);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        setCSV(csv);
    }

    public void setCSV(CSV csv){
        csv.addChangeListener(this);
        // ------------ Columns table ------------
        columnsModel = new ColumnsModel(csv);
        final JTable columnsTable = explorer.getColumnsTable();
        columnsTable.setModel(columnsModel);
        columnsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(final ListSelectionEvent e) {
                setSelectedColumn( explorer.getColumnsTable().getSelectedRow() );
            }
        });
        columnsTable.getColumnModel().getColumn(0).setMinWidth(32);
        columnsTable.getColumnModel().getColumn(0).setMaxWidth(32);

        columnsTable.getColumnModel().getColumn(2).setMinWidth(32);
        columnsTable.getColumnModel().getColumn(2).setMaxWidth(32);
//        columnsTable.getTableHeader().setAlignmentX(Component.RIGHT_ALIGNMENT);

        columnsTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        columnsTable.setDefaultRenderer( String.class, new SebraRenderer());
        columnsTable.setDefaultRenderer( Integer.class, new SebraRenderer());
        columnsTable.setRowHeight( 20 );

        // ------------ Unique Table -------------
        uniqueModel = new UniqueModel(csv);
        final JTable uniqueTable = explorer.getUniqueTable();
        uniqueTable.setModel(uniqueModel);
        uniqueTable.getColumnModel().getColumn(0).setMinWidth(32);
        uniqueTable.getColumnModel().getColumn(0).setMaxWidth(32);
        uniqueTable.getColumnModel().getColumn(1).setMinWidth(32);
        uniqueTable.getColumnModel().getColumn(2).setMinWidth(16);
        uniqueTable.getColumnModel().getColumn(2).setPreferredWidth(32);
        uniqueTable.getColumnModel().getColumn(2).setMaxWidth(64);
        uniqueTable.setDefaultRenderer( String.class, new SebraRenderer());
        uniqueTable.setDefaultRenderer( Integer.class, new SebraRenderer());
        uniqueTable.setRowHeight( 20 );
//        uniqueTable.setRowSelectionAllowed(false);
        uniqueTable.setColumnSelectionAllowed(false);

        // ------------ Results Table ------------
        resultsModel = new ResultsModel(csv);
        final JTable resultsTable = explorer.getResultsTable();
        resultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        resultsTable.setModel(resultsModel);
        resultsTable.setDefaultRenderer( Object.class, new SebraRenderer());
        resultsTable.setBackground(UIManager.getColor("Label.background"));
        resultsTable.setRowHeight( 20 );
        setSelectedColumn(0);
        explorer.rowsLabel.setText(csv.getRowCount() + "");
        explorer.columnsLabel.setText(csv.getMetaData().getColumnCount() + "");
        explorer.charsetLabel.setText(csv.getMetaData().getCharset() + "");
        explorer.separatorLabel.setText(csv.getMetaData().getSeparatorChar() + "");
        explorer.sizeLabel.setText(csv.getMetaData().getColumnCount() + "");



        // Drag drop for ordering columns
        resultsTable.setDragEnabled(true);
        resultsTable.setDropMode(DropMode.INSERT_ROWS);
        //resultsTable.setTransferHandler(new TableRowTransferHandler(table));
    }

    private void updateSize(){
        int y = 0;
        for (int x=0; x<csv.getMetaData().getColumnCount(); x++){
            Column c = csv.getMetaData().getColumn(x);
            if (c.isVisible() ){
                System.out.println( c.getWidth() );
                if (c.getWidth() > 0){
                    explorer.getResultsTable().getColumnModel().getColumn(y).setPreferredWidth( c.getWidth() );
                }
                y++;
            }
        }
    }

    public void setSelectedColumn(int selectedColumnIndex) {
        this.selectedColumnIndex = selectedColumnIndex;
        uniqueModel.setColumnIndex(selectedColumnIndex);
        uniqueModel.update();
    }

    @Override
    public void columnCreated(final Column column) {

    }

    @Override
    public void columnUpdated(final Column column) {
        System.out.println("Column.update: " + column.getName());
        resultsModel = new ResultsModel(csv);
        final JTable resultsTable = explorer.getResultsTable();
        resultsTable.setModel(resultsModel);
        updateSize();
    }

    @Override
    public void columnRemoved(final int columnIndex) {

    }

    @Override
    public void rowUpdated(final int rowIndex, final Row row) {

    }

    @Override
    public void rowRemoved(final int rowIndex, final Row row) {

    }

    @Override
    public void rowCreated(final int rowIndex, final Row row) {

    }

    @Override
    public void metaDataRead(final MetaData metaData) {

    }

    @Override
    public void cellUpdated(final int columnIndex, final int rowIndex) {

    }

    @Override
    public void beginRead(final File file) {

    }

    @Override
    public void finishRead(final File file) {

    }

    @Override
    public void beginWrite(final File file) {

    }

    @Override
    public void finishWrite(final File file) {

    }

}
