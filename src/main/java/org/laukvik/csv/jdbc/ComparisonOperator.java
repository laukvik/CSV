package org.laukvik.csv.jdbc;

public class ComparisonOperator extends Operator {
	
	public final static String TYPE_EQUALS 			= "=";
	public final static String TYPE_NOTEQUALS 		= "!=";
	public final static String TYPE_GREATER 		= ">";
	public final static String TYPE_GREATEREQUALS	= ">=";
	public final static String TYPE_LESS 			= "<";
	public final static String TYPE_LESSEQUALS 		= "<=";
	
	final static String [] VALID_TYPES = { TYPE_EQUALS, TYPE_NOTEQUALS, TYPE_GREATER, TYPE_GREATEREQUALS, TYPE_LESS, TYPE_LESSEQUALS };
	
	public ComparisonOperator( String operator ){
		for (String s : VALID_TYPES){
			if (!s.equalsIgnoreCase( operator )){
				throw new IllegalArgumentException( "Invalid comparison operator '" + operator + "'" );
			}
		}
		this.operator = operator;
	}

}