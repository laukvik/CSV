package org.laukvik.csv;

import java.io.File;
import java.io.FileFilter;


public class CSVFileFilter implements FileFilter  {


	public boolean accept(File f) {
		return (f.isFile() && f.getName().toLowerCase().endsWith(".csv"));
	}

	public String getDescription() {
		return "Shows CSV files";
	}

}
