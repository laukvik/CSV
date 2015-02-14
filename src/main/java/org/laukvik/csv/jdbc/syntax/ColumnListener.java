package org.laukvik.csv.jdbc.syntax;

import org.laukvik.csv.jdbc.Column;

public interface ColumnListener {

	public void found( Column column );
	
}