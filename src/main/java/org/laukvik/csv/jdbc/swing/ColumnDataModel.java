package org.laukvik.csv.jdbc.swing;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.laukvik.csv.jdbc.data.ColumnData;

public class ColumnDataModel implements TableModel {

    private ColumnData data;
    private List<TableModelListener> listeners;

    public ColumnDataModel(ColumnData data) {
        this.data = data;
        this.listeners = new ArrayList<>();
    }

    public void addTableModelListener(TableModelListener l) {
        this.listeners.add(l);
    }

    public void removeTableModelListener(TableModelListener l) {
        this.listeners.remove(l);
    }

    public ColumnData getColumnData() {
        return data;
    }

    public Class<?> getColumnClass(int column) {
        return String.class;
    }

    public int getColumnCount() {
        return data.listColumns().length;
    }

    public String getColumnName(int column) {
        return data.listColumns()[column].getName();
    }

    public int getRowCount() {
        return data.getRowCount();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.getValue(columnIndex, rowIndex);
    }

    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public void setValueAt(Object value, int row, int column) {

    }

}
