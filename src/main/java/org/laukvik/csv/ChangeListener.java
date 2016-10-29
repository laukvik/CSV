package org.laukvik.csv;

import org.laukvik.csv.columns.Column;

/**
 * Listener class for when the CSV data changes.
 *
 */
public interface ChangeListener {

    /**
     * Indicates the Column was created.
     *
     * @param column the column
     */
    void columnCreated(Column column);

    /**
     * Indicates the Column was updated.
     *
     * @param column the column
     */
    void columnUpdated(Column column);

    /**
     * Indicates the Column was removed.
     *
     * @param columnIndex the column index
     */
    void columnRemoved(int columnIndex);

    /**
     * Indicates that a Column was moved to a different position.
     *
     * @param fromRowIndex from row index
     * @param toRowIndex   to row index
     */
    void columnMoved(int fromRowIndex, int toRowIndex);

    /**
     * Indicates that the Row was removed.
     *
     * @param rowIndex the row index
     * @param row      the row
     */
    void rowRemoved(int rowIndex, Row row);

    /**
     * Indicates that the Row was created.
     *
     * @param rowIndex the row index
     * @param row      the row
     */
    void rowCreated(int rowIndex, Row row);

    /**
     * Indicates that the Row was moved.
     *
     * @param fromRowIndex from row index
     * @param toRowIndex to row index
     */
    void rowMoved(int fromRowIndex, int toRowIndex);

    /**
     * Indicates that the all rows between was removed.
     *
     * @param fromRowIndex from row index
     * @param toRowIndex to row index
     */
    void rowsRemoved(int fromRowIndex, int toRowIndex);

    /**
     * Indicates the MetaData was read.
     *
     * @param metaData the metaData
     */
    void metaDataRead(MetaData metaData);

    /**
     * The cell at the specified column index and row index was updated.
     *
     * @param columnIndex the column index
     * @param rowIndex the row index
     */
    void cellUpdated(int columnIndex, int rowIndex);

}
