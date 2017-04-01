package org.laukvik.csv.io;

import org.laukvik.csv.CSV;

import java.io.File;

/**
 * An interface for reading data sets.
 */
public interface DatasetFileReader {

    /**
     * Reads the entire file.
     *
     * @param file the csvFile
     * @param csv  the csv instance
     * @throws CsvReaderException when the file can't be fully read
     */
    void readFile(File file, CSV csv) throws CsvReaderException;

}
