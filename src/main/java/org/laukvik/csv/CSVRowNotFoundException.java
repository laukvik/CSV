package org.laukvik.csv;

public class CSVRowNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public CSVRowNotFoundException( int rowIndex ){
		super("Could not find row " + rowIndex );
	}
	
}