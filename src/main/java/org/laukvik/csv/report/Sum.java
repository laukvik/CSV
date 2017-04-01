package org.laukvik.csv.report;

import org.laukvik.csv.Row;
import org.laukvik.csv.columns.IntegerColumn;

import java.math.BigDecimal;

/**
 * Sums up the value of all aggregated rows.
 */
public final class Sum extends Aggregate {

    /**
     * Container for the sum.
     */
    private final BigDecimal sum;

    /**
     * Creates a new SUM for the column.
     *
     * @param column the column
     */
    public Sum(final IntegerColumn column) {
        super(column);
        sum = new BigDecimal(0);
    }

    @Override
    public void aggregate(final Row row) {
        Integer value = row.get((IntegerColumn) getColumn());
        sum.add(new BigDecimal(value == null ? 0  : value));
    }

    /**
     * Returns the value.
     *
     * @return the value
     */
    @Override
    public BigDecimal getValue() {
        return sum;
    }


}
