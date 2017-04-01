package org.laukvik.csv.report;

import org.laukvik.csv.Row;
import org.laukvik.csv.columns.IntegerColumn;

/**
 * Finds the maximum value of all aggregated rows.
 */
public final class Max extends Aggregate {

    /**
     * Container for the maximum value.
     */
    private Integer max;

    /**
     * Creates a maximum aggregator for the column.
     * @param column the column
     */
    public Max(final IntegerColumn column) {
        super(column);
    }

    /**
     * Returns the aggregated value.
     * @return the maximum value found
     */
    @Override
    public Integer getValue() {
        return max;
    }

    /**
     * Checks the value in the column.
     *
     * @param row the row.
     */
    @Override
    public void aggregate(final Row row) {
        Integer value = row.getInteger((IntegerColumn) getColumn());
        if (max == null) {
            max = value;
        } else {
            if (max < value) {
                max = value;
            }
        }
    }
}
