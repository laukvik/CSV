package org.laukvik.csv.sql;

import org.laukvik.csv.jdbc.data.ColumnData;

public abstract class Condition {

	
	public Condition(){
	}

	public abstract boolean accepts( ColumnData data, String [] values  );
	
}