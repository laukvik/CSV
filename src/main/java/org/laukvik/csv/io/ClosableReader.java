package org.laukvik.csv.io;

import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;

/**
 * An interface for reading data sets which is autoClosable.
 *
 * @author Morten Laukvik
 */
public interface ClosableReader extends AutoCloseable, Readable {

    MetaData getMetaData();
    boolean hasNext();
    Row getRow();
    int getBytesRead();

}
