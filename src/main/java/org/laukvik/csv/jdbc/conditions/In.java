package org.laukvik.csv.jdbc.conditions;

import org.laukvik.csv.jdbc.Column;
import org.laukvik.csv.jdbc.Condition;
import org.laukvik.csv.jdbc.data.ColumnData;
import org.laukvik.csv.jdbc.type.VarChar;

public class In extends Condition {

    Column column;
    VarChar[] value;

    public In(Column column, VarChar... value) {
        super();
        this.column = column;
        this.value = value;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();

        for (int x = 0; x < value.length; x++) {
            buffer.append(x > 0 ? ", '" + value[x] + "'" : "'" + value[x] + "'");
        }
        return column + " IN ( " + buffer.toString() + " )";
    }

//	public boolean accepts(ResultSetRow row) {
//		Value v = row.getValue( column );
//		if (v instanceof VarChar){
//			VarChar vc = (VarChar) v;
//			for (VarChar vch : value){
//				if (vch.equals( vc )){
//					return true;
//				}
//			}
//		}
//		return false;
//	}
    public boolean accepts(ColumnData data, String[] values) {
        String v = values[data.indexOf(column)];
        for (VarChar vc : value) {
            if (vc.toString().equals(v)) {
                return true;
            }
        }
        return false;
    }

}
