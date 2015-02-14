package org.laukvik.csv.jdbc.syntax;

/**
 * 
 * 
 * @author morten
 *
 */
public class ArrayReader extends GroupReader {

	public ArrayReader( String... items ){
		for (String s : items){
			addOptional( new TextReader( s ) );
		}
	}

}
