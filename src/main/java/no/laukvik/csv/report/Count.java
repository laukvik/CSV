package no.laukvik.csv.report;

import no.laukvik.csv.Row;
import no.laukvik.csv.columns.Column;

/**
 * Calculates the number of values.
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
     *
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

    @Override
    public String toString() {
        return "Count(" + getColumn().getName() + ")";
    }

}
