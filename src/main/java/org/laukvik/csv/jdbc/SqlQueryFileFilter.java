package org.laukvik.csv.jdbc;

import java.io.File;
import java.io.FileFilter;


public class SqlQueryFileFilter implements FileFilter  {


	public boolean accept(File f) {
		return (f.isFile() && f.getName().toLowerCase().endsWith(".sql"));
	}

	public String getDescription() {
		return "Shows SQL files";
	}

}
