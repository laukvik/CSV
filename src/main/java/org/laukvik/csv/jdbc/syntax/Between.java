package org.laukvik.csv.jdbc.syntax;

public class Between extends Reader {

	String left, right;
	Reader reader;
	
	public Between( String left, String right, Reader reader ){
		this.left = left;
		this.right = right;
		this.reader = reader;
	}

	public String consume(String sql) throws SyntaxException {
		int first = sql.indexOf( left );
		int last = sql.indexOf( right );
		if (first == 0 && last > first){
			String between = sql.substring( first+1, last );
			log( "Between:" + between + "" );
			reader.consume( between );
			sql = sql.substring( last+1 );
		} else {
			if (isRequired()){
				throw new SyntaxException( "Could not find both " + left + " and " + right );
			}
		}
		return sql;
	}

	public String getPurpose() {
		return "Uses a reader to consume contents between a left and right string";
	}

}
