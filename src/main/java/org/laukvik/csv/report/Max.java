package org.laukvik.csv.report;

import org.laukvik.csv.Row;
import org.laukvik.csv.columns.IntegerColumn;

/**
 * Finds the lowest value of the.
 */
public class Max extends Aggregate {

    /**
     * Container for the minimum value.
     */
    private Integer max;

    /**
     *
     * @param column
     */
    public Max(final IntegerColumn column) {
        super(column);
    }

    public Integer getValue() {
        return max;
    }

    @Override
    public void reset() {
        max = 0;
    }

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
