package org.laukvik.csv.report;

import org.laukvik.csv.Row;
import org.laukvik.csv.columns.IntegerColumn;

import java.math.BigDecimal;

/**
 * Calculates the average of all values.
 */
public final class Avg extends Aggregate {

    /**
     * Container for the total.
     */
    private BigDecimal sum;

    /**
     * The amount of values.
     */
    private long count;

    /**
     *
     * @param column the column
     */
    public Avg(final IntegerColumn column) {
        super(column);
        sum = new BigDecimal(0);
    }

    /**
     * Creates a new average based on the value in the row.
     *
     * @param row the row
     */
    @Override
    public void aggregate(final Row row) {
        Integer value = row.getInteger((IntegerColumn) getColumn());
        if (value != null) {
            sum = sum.add(new BigDecimal(value));
        }
        count++;
    }

    /**
     * Returns the value.
     *
     * @return the value
     */
    @Override
    public BigDecimal getValue() {
        if (count == 0) {
            return new BigDecimal(0);
        }
        return sum.divideToIntegralValue(new BigDecimal(count));
    }

}
