package org.laukvik.csv.columns;

/**
 * Indicates that the column name is already being used.
 */
public class ColumnAlreadyExistException extends IllegalStateException {

    /**
     * Indicates that the column name is already being used.
     *
     * @param column the column
     */
    public ColumnAlreadyExistException(final Column column) {
        super("A column with the name " + column.getName() + " already exist!");
    }
}
