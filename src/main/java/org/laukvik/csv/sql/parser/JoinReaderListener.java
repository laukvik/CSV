package org.laukvik.csv.sql.parser;

import org.laukvik.csv.sql.Join;

public interface JoinReaderListener {

	public void found( Join join );
	
}