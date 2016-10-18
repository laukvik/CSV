package org.laukvik.csv.io;

import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.StringColumn;

import java.io.File;

/**
 * Reads a data set using fixed width columns.
 *
 * @author Morten Laukvik
 */
public class FixedWidthReader implements ClosableReader {

    private final int[] columnWidths;
    private final MetaData metaData;

    /**
     * Reads CSV data from the reader with the specified columns
     *
     * @param columnWidths the width of each column
     */
    public FixedWidthReader(int... columnWidths) {
        this.columnWidths = columnWidths;
        this.metaData = new MetaData();
        for (int x = 0; x < columnWidths.length; x++) {
            StringColumn s = new StringColumn("Column" + (x + 1));
            s.setSize(columnWidths[x]);
            this.metaData.addColumn(s);
        }
    }

    @Override
    public void readFile(final File file) {

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
