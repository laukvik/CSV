package org.laukvik.csv.jdbc.conditions;

import org.laukvik.csv.jdbc.Column;
import org.laukvik.csv.jdbc.Condition;
import org.laukvik.csv.jdbc.Value;
import org.laukvik.csv.jdbc.data.ColumnData;

public class Greater extends Condition{
	
	Column column; 
	Value value;
	
	public Greater( Column column, Value value ){
		super();
		this.column = column;
		this.value = value;
	}
	
	public String toString(){
		return column + " > " + value;
	}

	public boolean accepts( ColumnData data, String [] values ) {
		return false;
	}

}