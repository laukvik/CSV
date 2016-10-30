package org.laukvik.csv.io;

import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.StringColumn;

import java.io.File;

/**
 * Reads a data set using fixed width columns.
 *
 * TODO - implement this class
 */
public final class FixedWidthReader implements ClosableReader {

    /**
     * The MetaData.
     */
    private final MetaData metaData;

    /**
     * Reads CSV data from the reader with the specified columns.
     *
     * @param columnWidths the width of each column
     */
    public FixedWidthReader(final int... columnWidths) {
        final int[] columnWidths1 = columnWidths;
        this.metaData = new MetaData();
        for (int x = 0; x < columnWidths.length; x++) {
            StringColumn s = new StringColumn("Column" + (x + 1));
            s.setSize(columnWidths[x]);
            this.metaData.addColumn(s);
        }
    }

    /**
     * Reads a file with fixed column widths.
     *
     * @param file the file
     */
    public void readFile(final File file) {

    }

    /**
     * Returns the MetaData.
     *
     * @return the MetaData
     */
    public MetaData getMetaData() {
        return metaData;
    }

    /**
     * Returns true if more rows are available.
     * @return true if more rows are available
     */
    public boolean hasNext() {
        return false;
    }

    /**
     * Returns the next row.
     * @return the next row
     */
    public Row next() {
        return null;
    }

    /**
     * Returns the current row.
     * @return the current row
     */
    public Row getRow() {
        return null;
    }

    /**
     * Returns the amount of bytes read.
     * @return the amount of bytes read
     */
    public int getBytesRead() {
        return 0;
    }

    /**
     * Closes the outputStream.
     *
     * @throws Exception when the outputStream could not be closed
     */
    public void close() throws Exception {

    }
}
