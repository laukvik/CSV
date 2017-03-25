package org.laukvik.csv.io;

import org.laukvik.csv.CSV;

import java.io.File;
import java.io.IOException;

/**
 * An interface for reading an entire dataset.
 */
public interface DatasetFileReader {

    /**
     * Reads the entire file.
     *
     * @param file the csvFile
     * @param csv  the csv instance
     * @throws IOException when the file can't be fully read
     */
    void readFile(File file, CSV csv) throws CsvReaderException;

}
