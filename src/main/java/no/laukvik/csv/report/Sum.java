package no.laukvik.csv.report;

import no.laukvik.csv.Row;
import no.laukvik.csv.columns.IntegerColumn;

import java.math.BigDecimal;

/**
 * Calculates the sum of all values.
 */
public final class Sum extends Aggregate {

    /**
     * Container for the sum.
     */
    private BigDecimal sum;

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

        Object value = row.getObject(getColumn());
        if (value != null) {

            if (value instanceof Integer) {
                Integer i = (Integer) value;

                if (sum == null) {
                    sum = new BigDecimal(i);
                } else {
                    sum = sum.add(new BigDecimal(i));
                }

            } else if (value instanceof Double) {
                Double i = (Double) value;

                if (sum == null) {
                    sum = new BigDecimal(i);
                } else {
                    sum = sum.add(new BigDecimal(i));
                }

            } else if (value instanceof Float) {
                Float i = (Float) value;

                if (sum == null) {
                    sum = new BigDecimal(i);
                } else {
                    sum = sum.add(new BigDecimal(i));
                }

            } else if (value instanceof BigDecimal) {
                BigDecimal i = (BigDecimal) value;

                if (sum == null) {
                    sum = i;
                } else {
                    sum = sum.add(i);
                }

            }

        }

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

    @Override
    public String toString() {
        return "Sum(" + getColumn().getName() + ")";
    }

}
