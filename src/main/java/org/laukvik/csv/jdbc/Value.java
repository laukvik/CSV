package org.laukvik.csv.jdbc;

public abstract class Value {
	
	public Value( String value ){
	}
	
	public abstract boolean equals( Value value );
	public abstract boolean greater( Value value );
	public abstract boolean less( Value value );

	public abstract String toString();
	
}