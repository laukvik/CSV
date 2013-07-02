package org.laukvik.csv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

public class CSV implements CSVListener{
	
	String [] headers;
	Vector<String[]> rows;
	CSVReader reader;
	
	public CSV(){
		headers = null;
		rows = new Vector<String[]>();
		reader = new CSVReader();
		reader.addCSVListener( this );
	}
	
	public void parse( InputStream is ) throws IOException{
		reader.read( is );
	}
	
	public void parse( File file ) throws FileNotFoundException, IOException{
		reader.read( new FileInputStream( file ) );
	}
	
	public void write( File file ) throws IOException{
		
		FileOutputStream fos = new FileOutputStream( file );
		write( fos );
		fos.close();
		
	}
	
	public void write( OutputStream out ) throws IOException{	
		if (isHeadersAvailable()){
			for (int x=0; x<headers.length; x++){
				if (x>0){
					out.write( (CSVReader.COMMA+"").getBytes() );
				}
				out.write( encode( headers[ x ] ).getBytes() );
			}
			out.write( CSVReader.LF );
		}
		for (int y=0; y<rows.size(); y++){
			if (y>0){
//				out.write( CSVReader.LF );
			}
			String [] row = rows.get( y );
			for (int x=0; x<row.length; x++){
				if (x>0){
					out.write( (CSVReader.COMMA+"").getBytes() );
				}
				out.write( encode( row[ x ] ).getBytes() );
			}
			out.write( CSVReader.LF );
		}
	}

	public static String encode( String value ){
		if (value == null){
			return null;
		} else if (value.length() == 0){
			return "";
		} else {
			String newValue = value;
			if (value.contains( "\"" )){
				newValue =  newValue.replaceAll( "\"", "\"\"" );
			}
			if (value.contains( "," )){
				newValue =  "\"" + newValue + "\"";
			}
			return newValue;
		}
	}

	public void foundHeaders( String[] values){
		headers = values;
	}

	public void foundRow(int rowIndex, String[] values) {
		rows.add( values );
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	public int getColumnCount() {
		if (headers == null){
			return rows.get( 0 ).length;
		} else {
			return headers.length;
		}
	}

	public boolean isHeadersAvailable() {
		return headers != null;
	}
	
	public String getHeader( int columnIndex ){
		return headers[ columnIndex ];
	}
	
	public String getCell( int column, int row ) throws CSVRowNotFoundException, CSVColumnNotFoundException{
		if (row > rows.size()){
			throw new CSVRowNotFoundException( row );
		} else {
			String [] r = rows.get( row );
			if (column > r.length){
				throw new CSVColumnNotFoundException( column, row );
			} else {
				return r[ column ];		
			}
		}
	}

	public int getRowCount() {
		return rows.size();
	}

	public void setCell(String value, int column, int row) {
		rows.get( row )[ column ] = value;
	}

}