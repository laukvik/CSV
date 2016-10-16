package org.laukvik.csv.io;

import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.StringColumn;

import java.io.BufferedReader;

/**
 * @author Morten Laukvik
 */
public class DataReader implements AbstractReader {

    private final int[] columnWidths;
    private final MetaData metaData;

    public DataReader(final BufferedReader reader, int... columnWidths) {
        this.columnWidths = columnWidths;
        this.metaData = new MetaData();
        for (int x = 0; x < columnWidths.length; x++) {
            StringColumn s = new StringColumn("Column" + (x + 1));
            s.setSize(columnWidths[x]);
            this.metaData.addColumn(s);
        }
    }

    @Override
    public MetaData getMetaData() {
        return metaData;
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
