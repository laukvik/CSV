package org.laukvik.csv.sql.conditions;

import org.laukvik.csv.sql.Condition;
import org.laukvik.csv.jdbc.data.ColumnData;

public class Or extends Condition{

	Condition conditionA;
	Condition conditionB;
	
	public Or(Condition conditionA, Condition conditionB) {
		this.conditionA = conditionA;
		this.conditionB = conditionB;
	}

//	public boolean accepts(ResultSetRow row) {
//		return conditionA.accepts(row) || conditionB.accepts(row);
//	}
	
	public boolean accepts( ColumnData data, String [] values ) {
		return false;
	}
	
	public String toString(){
		return "(" + conditionA + " OR " + conditionB + ")";
	}

}
