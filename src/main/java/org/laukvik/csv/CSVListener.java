package org.laukvik.csv;

public interface CSVListener {

	public void foundHeaders( String [] values );
	public void foundRow( int rowIndex, String [] values );
	
}