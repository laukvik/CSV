package org.laukvik.csv.io;

import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Morten Laukvik
 */
public interface DatasetOutputStream {

    /**
     * Writes the single row to the outputStream.
     *
     * @param row          the row
     * @param metaData     the metadata
     * @param outputStream the outputStream
     * @throws IOException when the row can't be written
     */
    void writeCSV(Row row, MetaData metaData, OutputStream outputStream) throws IOException;


}
