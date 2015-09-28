package org.laukvik.csv.sql.conditions;

import org.laukvik.csv.sql.Condition;
import org.laukvik.csv.jdbc.data.ColumnData;

public class Not extends Condition {

	Condition conditionA;
	
	public Not(Condition conditionA ) {
		this.conditionA = conditionA;
	}
	
	public boolean accepts(  ColumnData data, String [] values ) {
		return false;
	}
	
	public String toString(){
		return "NOT (" + conditionA + ")";
	}

}
