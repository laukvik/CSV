package org.laukvik.csv.io;

import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;

import java.util.List;

/**
 * Reads a data set by a list of objects.
 *
 * @author Morten Laukvik
 */
public class JavaReader<T> implements ClosableReader {

    private CsvReader reader;

    /**
     * @param values
     */
    public JavaReader(List<T> values) {

    }

    @Override
    public MetaData getMetaData() {
        return null;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Row next() {
        return null;
    }

    @Override
    public Row getRow() {
        return null;
    }

    @Override
    public int getBytesRead() {
        return 0;
    }

    @Override
    public void close() throws Exception {

    }
}
