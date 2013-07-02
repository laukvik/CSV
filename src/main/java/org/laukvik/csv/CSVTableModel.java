package org.laukvik.csv;

import java.util.Vector;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class CSVTableModel implements TableModel {
	
	private CSV csv;
	private Vector <TableModelListener> listeners;
	
	public CSVTableModel( CSV csv ){
		this.csv = csv;
		this.listeners = new Vector<TableModelListener>();
	}

	public void addTableModelListener( TableModelListener l ) {
		this.listeners.add( l );
	}
	
	public void removeTableModelListener(TableModelListener l) {
		this.listeners.remove( l );
	}
	
	public CSV getCSV() {
		return csv;
	}

	public Class<?> getColumnClass( int column ){
		return String.class;
	}

	public int getColumnCount() {
		return csv.getColumnCount();
	}

	public String getColumnName(int column) {
		if (csv.isHeadersAvailable()){
			return csv.getHeader( column );
		} else {
			return null;
		}
	}

	public int getRowCount() {
		return csv.getRowCount();
	}

	public Object getValueAt( int row, int column ) {
		try {
			return csv.getCell( column, row );
		} catch (Exception e) {
			return null;
		}
	}

	public boolean isCellEditable(int row, int column ) {
		return true;
	}

	public void setValueAt( Object value, int row, int column ) {
		csv.setCell( (String) value, column, row );
	}

}