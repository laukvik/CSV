package org.laukvik.csv;

import org.laukvik.csv.columns.Column;

import java.io.File;

/**
 * Listener class for when the CSV data changes.
 *
 * @author Morten Laukvik
 */
public interface ChangeListener {

    void columnCreated(final Column column);
    void columnUpdated(final Column column);
    void columnRemoved(final int columnIndex);
    void columnMoved(int fromRowIndex, int toRowIndex);

    void rowUpdated(int rowIndex, final Row row);
    void rowRemoved(int rowIndex, final Row row);
    void rowCreated(int rowIndex, final Row row);
    void rowMoved(int fromRowIndex, int toRowIndex);

    void metaDataRead(final MetaData metaData);

    void cellUpdated(int columnIndex, int rowIndex);

    void beginRead(final File file);
    void finishRead(final File file);

    void beginWrite(final File file);
    void finishWrite(final File file);

}
