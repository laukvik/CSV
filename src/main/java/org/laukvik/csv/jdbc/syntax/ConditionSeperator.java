package org.laukvik.csv.jdbc.syntax;

public class ConditionSeperator extends GroupReader {
	
	public ConditionSeperator(){
		super();
		add( new EmptyReader() );
		add( new ArrayReader( "AND", "OR", "NOT" ) );
		add( new EmptyReader() );
	}

	public String getPurpose() {
		return "Consume all AND, OR, NOT, >, <";
	}

}