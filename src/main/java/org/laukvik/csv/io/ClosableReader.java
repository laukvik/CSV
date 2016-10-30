package org.laukvik.csv.io;

import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;

/**
 * An interface for reading data sets which is autoClosable.
 *
 * @author Morten Laukvik
 */
public interface ClosableReader extends AutoCloseable, Readable {

    /**
     * Returns the MetaData.
     *
     * @return the MetaData
     */
    MetaData getMetaData();

    /**
     * Returns true if more rows can be read.
     *
     * @return true if more rows
     */
    boolean hasNext();

    /**
     * Returns the current row.
     *
     * @return the row
     */
    Row getRow();

    /**
     * Returns the amount of bytes read.
     *
     * @return the amount of bytes
     */
    int getBytesRead();

}
