package org.laukvik.csv;

import org.laukvik.csv.columns.Column;

/**
 *
 *
 * @author Morten Laukvik
 */
public interface ChangeListener {

    void columnCreated(Column column);
    void columnUpdated(Column column);
    void columnRemoved(Column column);

    void rowUpdated(int rowIndex, Row row);
    void rowRemoved(int rowIndex, Row row);
    void rowCreated(int rowIndex, Row row);

    void metaDataRead(MetaData metaData);

}
