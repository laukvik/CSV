package org.laukvik.csv.ui;

import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.Column;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Displays all columns in a CSV. Can toggle visibility of columns on or off.
 *
 * @author Morten Laukvik
 */
public class ColumnsModel implements TableModel{

    private CSV csv;
    private List<TableModelListener> listeners;

    public ColumnsModel( CSV csv ) {
        this.csv = csv;
        this.listeners = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return csv.getMetaData().getColumnCount();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(final int columnIndex) {
        switch (columnIndex){
            case 0 : return "Visible";
            case 1 : return "Column";
            case 2 : return "Width";
            case 3 : return "Type";
            default : return "";
        }
    }

    @Override
    public Class<?> getColumnClass(final int columnIndex) {
        switch (columnIndex){
            case 0 : return Boolean.class;
            case 1 : return String.class;
            case 2 : return Integer.class;
            case 3 : return String.class;
            default : return Void.class;
        }
    }

    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return true;
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        Column c = getRow(rowIndex);
        switch (columnIndex){
            case 0 : return c.isVisible();
            case 1 : return c.getName();
            case 2 : return c.getWidth();
            case 3 : return c.getClass().getSimpleName();
            default : return Void.class;
        }
    }

    private Column getRow(int rowIndex){
        return csv.getMetaData().getColumn(rowIndex);
    }

    @Override
    public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
        Column c = csv.getMetaData().getColumn(rowIndex);
        switch (columnIndex){
            case 0 : c.setVisible((boolean) aValue); break;
            case 1 : c.setName( (String) aValue); break;
            case 2 : c.setWidth( (Integer) aValue); break;
        }
    }

    @Override
    public void addTableModelListener(final TableModelListener l) {
        listeners.add(l);
    }

    @Override
    public void removeTableModelListener(final TableModelListener l) {
        listeners.remove(l);
    }
}
