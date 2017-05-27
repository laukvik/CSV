package no.laukvik.csv.report;

import no.laukvik.csv.Row;
import no.laukvik.csv.columns.Column;

import java.math.BigDecimal;

/**
 * Calculates the average of all values.
 *
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
     * The IntegerColumn to aggregate
     *
     * @param column the column
     */
    public Avg(final Column column) {
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
        Object value = row.getObject(getColumn());
        if (value != null) {
            if (value instanceof Integer) {
                sum = sum.add(new BigDecimal((Integer) value));
            } else if (value instanceof Double) {
                sum = sum.add(new BigDecimal((Double) value));
            } else if (value instanceof Float) {
                sum = sum.add(new BigDecimal((Float) value));
            } else if (value instanceof BigDecimal) {
                sum = sum.add((BigDecimal) value);
            }
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

    /**
     * Returns the amount of rows counted.
     *
     * @return the amount of rows
     */
    public long getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "Avg(" + getColumn().getName() + ")";
    }

}
