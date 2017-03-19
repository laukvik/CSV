package org.laukvik.csv.io;

import org.laukvik.csv.CSV;

/**
 * @author Morten Laukvik
 */
public interface DatasetReader {

    /**
     * Reads the dataset from the CSV.
     * @param csv the csv
     */
    void readDataset(CSV csv);

}
