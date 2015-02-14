package org.laukvik.csv.jdbc.data;

import org.laukvik.csv.jdbc.Column;

public interface ColumnData {

	public Class<?> getColumnClass( int columnIndex );
	public int indexOf( Column column );
	public Column [] listColumns();
	public Column getColumn( int columnIndex );
	public String [] getRow( int rowIndex );
	public int getRowCount();
	public int getColumnCount();
	public String getValue( int columnIndex, int rowIndex );
	public String getValue( Column column, int rowIndex );
	
}