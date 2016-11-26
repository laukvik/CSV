package org.laukvik.csv.report;

import org.laukvik.csv.Row;
import org.laukvik.csv.columns.IntegerColumn;

/**
 * Finds the lowest value of the.
 */
public final class Min extends Aggregate {

    /**
     * Container for the minimum value.
     */
    private Integer min;

    /**
     *
     * @param column
     */
    public Min(final IntegerColumn column) {
        super(column);
    }


    public Integer getValue() {
        return min;
    }

    @Override
    public void reset() {
        min = 0;
    }

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
