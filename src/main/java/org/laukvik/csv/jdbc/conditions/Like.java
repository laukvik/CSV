package org.laukvik.csv.jdbc.conditions;

import org.laukvik.csv.jdbc.Column;
import org.laukvik.csv.jdbc.Condition;
import org.laukvik.csv.jdbc.data.ColumnData;
import org.laukvik.csv.jdbc.type.VarChar;

public class Like extends Condition{
	
	Column column; 
	VarChar value;
	
	public Like( Column column, VarChar value ){
		super();
		this.column = column;
		this.value = value;
	}

	public boolean isTrue(){
		return false;
	}
	
	public String toString(){
		return column + " = " + value;
	}

//	public boolean accepts(ResultSetRow row) {
//		Value v = row.getValue( column );
//		if (v instanceof VarChar){
//			VarChar vc = (VarChar) v;
//			return vc.like( value  );
//		}
//		return false;
//	}

	public boolean accepts( ColumnData data, String [] values ) {
		return false;
	}
	
}