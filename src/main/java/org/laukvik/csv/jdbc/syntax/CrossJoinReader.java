package org.laukvik.csv.jdbc.syntax;

import org.laukvik.csv.jdbc.ParseException;
import org.laukvik.csv.jdbc.Table;
import org.laukvik.csv.jdbc.joins.CrossJoin;

public class CrossJoinReader extends JoinReader {

	String table;
	
	public CrossJoinReader() {
		add( new TextReader("CROSS JOIN") );
		addEmpty();
		add( new WordReader() ).addReaderListener( new ReaderListener(){
			public void found(String values) {
				table = values;
			}}  );
	}
	
	public CrossJoin getJoin() throws ParseException{
		return new CrossJoin( null, Table.parse( table ) );
	}

}