package org.laukvik.csv.report;

import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;

/**
 * Counts all items.
 *
 */
public final class Count extends Aggregate {

    /**
     * The amount of items.
     */
    private int count;

    /**
     * Sets the column to count.
     *
     * @param column the column
     */
    public Count(final Column column) {
        super(column);
        count = 0;
    }

    /**
     * Aggregates the column in the row.
     * @param row the row
     */
    public void aggregate(final Row row) {
        count++;
    }

    /**
     * The amount of items found.
     *
     * @return the amount
     */
    public Integer getValue() {
        return count;
    }

}
