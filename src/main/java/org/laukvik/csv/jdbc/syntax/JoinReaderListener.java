package org.laukvik.csv.jdbc.syntax;

import org.laukvik.csv.jdbc.Join;

public interface JoinReaderListener {

	public void found( Join join );
	
}