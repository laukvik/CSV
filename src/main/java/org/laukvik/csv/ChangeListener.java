package org.laukvik.csv;

import org.laukvik.csv.columns.Column;

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

    void rowRemoved(int rowIndex, final Row row);
    void rowCreated(int rowIndex, final Row row);
    void rowMoved(int fromRowIndex, int toRowIndex);
    void rowsRemoved(int fromRowIndex, int toRowIndex);

    void metaDataRead(final MetaData metaData);

    void cellUpdated(int columnIndex, int rowIndex);

}
