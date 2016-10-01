package org.laukvik.csv.ui;

import org.laukvik.csv.CSV;
import org.laukvik.csv.DistinctColumnValues;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Morten Laukvik
 */
public class UniqueModel implements TableModel{

    private CSV csv;
    private DistinctColumnValues values;
    private int columnIndex;
    private List<TableModelListener> listeners;
    private List<String> keys;

    public UniqueModel(CSV csv) {
        listeners = new ArrayList<>();
        setCSV(csv);
    }

    public void setColumnIndex( int columnIndex ){
        this.columnIndex = columnIndex;
    }

    public void setCSV(CSV csv){
        this.csv = csv;
        this.columnIndex = -1;
    }

    public void update(){
        if (columnIndex > -1){
            this.values = csv.getDistinctColumnValues(columnIndex);
        } else {
            values = new DistinctColumnValues();
        }
        keys = new ArrayList<>();
        for (String s : values.getKeys()){
            keys.add(s);
        }
        for (TableModelListener l : listeners){
            l.tableChanged( new TableModelEvent(this));
        }
    }

    @Override
    public int getRowCount() {
        return keys == null ? 0 : keys.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(final int columnIndex) {
        if (columnIndex == 0){
            return "";
        } else if (columnIndex == 1) {
            return "Value";
        } else {
            return "Count";
        }
    }

    @Override
    public Class<?> getColumnClass(final int columnIndex) {
        if (columnIndex == 0){
            return Boolean.class;
        } else if (columnIndex == 0){
            return String.class;
        } else {
            return Integer.class;
        }
    }

    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        if (columnIndex == 0){
            return false;
        } else if (columnIndex == 1){
            return keys.get(rowIndex);
        } else {
            return values.getCount(keys.get(rowIndex));
        }
    }

    @Override
    public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {

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
