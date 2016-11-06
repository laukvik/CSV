package org.laukvik.csv.io;

import org.laukvik.csv.CSV;

import java.io.File;

/**
 * An interface for reading an entire dataset.
 */
public interface DatasetFileReader {

    /**
     * Reads the entire file.
     *
     * @param file the csvFile
     * @param csv  the csv instance
     * @return the CSV
     */
    void readFile(CSV csv, File file);

}
