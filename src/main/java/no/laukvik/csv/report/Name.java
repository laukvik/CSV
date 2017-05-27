package no.laukvik.csv.report;

import no.laukvik.csv.Row;
import no.laukvik.csv.columns.Column;

/**
 * Returns the name of the column.
 */
public final class Name extends Aggregate {

    /**
     * @param column the column
     */
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

    @Override
    public String toString() {
        return "Name(" + getColumn().getName() + ")";
    }
}
