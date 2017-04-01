package org.laukvik.csv.report;

import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;

/**
 * Finds the amount of aggregated rows.
 *
 */
public final class Count extends Aggregate {

    /**
     * The amount of items.
     */
    private int amount;

    /**
     * Sets the column to amount.
     *
     * @param column the column
     */
    public Count(final Column column) {
        super(column);
        amount = 0;
    }

    /**
     * Aggregates the column in the row.
     * @param row the row
     */
    @Override
    public void aggregate(final Row row) {
        amount++;
    }

    /**
     * The amount of items found.
     *
     * @return the amount
     */
    @Override
    public Integer getValue() {
        return amount;
    }

}
