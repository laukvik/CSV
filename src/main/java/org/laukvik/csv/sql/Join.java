package org.laukvik.csv.sql;

import org.laukvik.csv.jdbc.data.ColumnData;

public abstract class Join {
	
	public Column left;
	public Column right;

	public Join( Column left, Column right ){
		this.left = left;
		this.right = right;
	}
	
	public Column getLeft() {
		return left;
	}
	
	public Column getRight() {
		return right;
	}

	public abstract String toString();
	
	/**
	 * Merges two sets of data
	 * 
	 * @param data
	 * @param right
	 * @return
	 */
	public abstract ColumnData join( ColumnData data, ColumnData right );
	
	public static Join parse( String sql ){
		return null;
	}
	
}