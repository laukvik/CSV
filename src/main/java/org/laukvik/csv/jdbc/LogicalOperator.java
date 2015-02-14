package org.laukvik.csv.jdbc;

public class LogicalOperator extends Operator {
	
	public final static String TYPE_AND = "AND";
	public final static String TYPE_OR  = "OR";
	public final static String TYPE_NOT = "NOT";
	
	final static String [] VALID_TYPES = { TYPE_AND, TYPE_OR, TYPE_NOT };
	
	public final static LogicalOperator AND = new LogicalOperator( TYPE_AND );
	public final static LogicalOperator OR = new LogicalOperator( TYPE_OR );
	public final static LogicalOperator NOT = new LogicalOperator( TYPE_NOT );

	public LogicalOperator( String operator ){
		for (String s : VALID_TYPES){
			if (!s.equalsIgnoreCase( operator )){
				throw new IllegalArgumentException( "Invalid logical operator '" + operator + "'" );
			}
		}
		this.operator = operator;
	}

	
}