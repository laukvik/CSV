package org.laukvik.csv.sql.parser;

import org.laukvik.csv.sql.Column;

public interface ColumnListener {

	public void found( Column column );
	
}