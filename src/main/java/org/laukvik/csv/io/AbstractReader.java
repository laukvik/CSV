package org.laukvik.csv.io;

import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;

/**
 * An interface for reading data sets into the CSV format
 *
 * @author Morten Laukvik
 */
public interface AbstractReader extends AutoCloseable, Readable{

    MetaData getMetaData();
    boolean hasNext();
    Row getRow();
    int getBytesRead();

}
