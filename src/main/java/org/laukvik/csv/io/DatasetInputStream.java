package org.laukvik.csv.io;

import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;

import java.io.InputStream;

/**
 *
 *
 */
public interface DatasetInputStream extends AutoCloseable {

    /**
     * Reads the next row from the inputStream.
     *
     * @param csv         the csv instance
     * @param inputStream the inputStream
     * @return the row
     */
    Row readCSV(CSV csv, InputStream inputStream);


}
