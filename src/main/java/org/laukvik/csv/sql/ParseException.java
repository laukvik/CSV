package org.laukvik.csv.sql;

import java.sql.SQLException;

public class ParseException extends SQLException {

	private static final long serialVersionUID = 1L;

	public ParseException( String message ){
		super( message );
	}
	
}