package org.laukvik.csv;

import org.laukvik.csv.columns.Column;

import java.io.File;

/**
 *
 *
 * @author Morten Laukvik
 */
public interface ChangeListener {

    void columnCreated(final Column column);
    void columnUpdated(final Column column);
    void columnRemoved(final int columnIndex);

    void rowUpdated(int rowIndex, final Row row);
    void rowRemoved(int rowIndex, final Row row);
    void rowCreated(int rowIndex, final Row row);

    void metaDataRead(final MetaData metaData);

    void cellUpdated(int columnIndex, int rowIndex);

    void beginRead(final File file);
    void finishRead(final File file);

    void beginWrite(final File file);
    void finishWrite(final File file);

}
