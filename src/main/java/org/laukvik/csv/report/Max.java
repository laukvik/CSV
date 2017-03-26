package org.laukvik.csv.report;

import org.laukvik.csv.Row;
import org.laukvik.csv.columns.IntegerColumn;

/**
 * Finds the lowest value of the.
 */
public final class Max extends Aggregate {

    /**
     * Container for the minimum value.
     */
    private Integer max;

    /**
     * Creates a new Max instance for with the column.
     * @param column the column
     */
    public Max(final IntegerColumn column) {
        super(column);
    }

    /**
     * Returns the aggregated value.
     * @return the maximum value found
     */
    public Integer getValue() {
        return max;
    }

    /**
     * Checks the value in the column.
     *
     * @param row the row.
     */
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
