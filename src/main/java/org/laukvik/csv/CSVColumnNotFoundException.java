package org.laukvik.csv;

public class CSVColumnNotFoundException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public CSVColumnNotFoundException( int columnIndex, int rowIndex ){
		super("Could not find column " + columnIndex + " in row " + rowIndex );
	}

}