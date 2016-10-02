package org.laukvik.csv.io;

import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;

import java.io.File;

/**
 *
 * @author Morten Laukvik
 */
public interface AbstractReader extends AutoCloseable, Readable{

    MetaData getMetaData();
    boolean hasNext();
    Row getRow();
    int getBytesRead();
    File getFile();

}
