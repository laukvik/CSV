package org.laukvik.csv.report;

import org.laukvik.csv.Row;
import org.laukvik.csv.columns.IntegerColumn;

/**
 * Finds the minimum value of all aggregated rows.
 */
public final class Min extends Aggregate {

    /**
     * Container for the minimum value.
     */
    private Integer min;

    /**
     * Creates a new Min with the column.
     *
     * @param column the column
     */
    public Min(final IntegerColumn column) {
        super(column);
    }


    /**
     * Returns the value.
     *
     * @return the value
     */
    @Override
    public Integer getValue() {
        return min;
    }

    /**
     * Aggregates the value in the row.
     *
     * @param row the row
     */
    @Override
    public void aggregate(final Row row) {
        Integer value = row.getInteger((IntegerColumn) getColumn());
        if (min == null) {
            min = value;
        } else {
            if (min > value) {
                min = value;
            }
        }
    }
}
