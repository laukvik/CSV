package org.laukvik.csv.jdbc.type;

import org.laukvik.csv.sql.Value;

public class SmallInt extends Value {

	int value;
	
	public SmallInt( int value ){
		super("");
		this.value = value;
	}

	public SmallInt(String value){
		super(value);
		Float f = Float.parseFloat( value );
		this.value = f.intValue();
	}

	public String toString() {
		return value + "";
	}
	
	public boolean equals(Value obj) {
		if (obj instanceof SmallInt){
			SmallInt v = (SmallInt) obj;
			return this.value == v.value;
		} else {
			return false;
		}
	}

	public boolean greater(Value value) {
		if (value instanceof SmallInt){
			SmallInt v = (SmallInt) value;
			return this.value > v.value;
		}
		return false;
	}

	public boolean less(Value value) {
		if (value instanceof SmallInt){
			SmallInt v = (SmallInt) value;
			return this.value < v.value;
		}
		return false;
	}
	
}