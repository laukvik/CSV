package org.laukvik.csv.jdbc;

import java.sql.SQLException;

public class ParseException extends SQLException {

	private static final long serialVersionUID = 1L;

	public ParseException( String message ){
		super( message );
	}
	
}