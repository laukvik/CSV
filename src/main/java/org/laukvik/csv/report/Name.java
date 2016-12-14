package org.laukvik.csv.report;

import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;

/**
 * Returns the value of the column.
 *
 */
public final class Name extends Aggregate {

    private Object name;

    public Name(final Column column) {
        super(column);
    }

    @Override
    public void aggregate(final Row row) {
    }

    @Override
    public String getValue() {
        return getColumn().getName();
    }

}
