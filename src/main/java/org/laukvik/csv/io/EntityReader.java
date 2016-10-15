package org.laukvik.csv.io;

import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;

import java.io.File;

/**
 * @author Morten Laukvik
 */
public class EntityReader implements AbstractReader{

    private final File file;
    private CsvReader reader;

    public EntityReader(final File file) {
        this.file = file;
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
