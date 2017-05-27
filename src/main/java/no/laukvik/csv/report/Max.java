package no.laukvik.csv.report;

import no.laukvik.csv.Row;
import no.laukvik.csv.columns.Column;

import java.math.BigDecimal;

/**
 * Calculates the maximum of all values.
 */
public final class Max extends Aggregate {

    /**
     * Container for the maximum value.
     */
    private BigDecimal max;

    /**
     * Creates a maximum aggregator for the column.
     *
     * @param column the column
     */
    public Max(final Column column) {
        super(column);
    }

    /**
     * Returns the aggregated value.
     *
     * @return the maximum value found
     */
    @Override
    public BigDecimal getValue() {
        return max;
    }

    /**
     * Checks the value in the column.
     *
     * @param row the row.
     */
    @Override
    public void aggregate(final Row row) {
        Object value = row.getObject(getColumn());
        if (value != null) {

            if (value instanceof Integer) {
                Integer i = (Integer) value;

                if (max == null) {
                    max = new BigDecimal(i);
                } else if (max.intValue() < i) {
                    max = new BigDecimal(i);
                }

            } else if (value instanceof Double) {
                Double i = (Double) value;

                if (max == null) {
                    max = new BigDecimal(i);
                } else if (max.doubleValue() < i) {
                    max = new BigDecimal(i);
                }

            } else if (value instanceof Float) {
                Float i = (Float) value;

                if (max == null) {
                    max = new BigDecimal(i);
                } else if (max.floatValue() < i) {
                    max = new BigDecimal(i);
                }

            } else if (value instanceof BigDecimal) {
                BigDecimal i = (BigDecimal) value;

                if (max == null) {
                    max = i;
                } else if (max.compareTo(i) < 0) {
                    max = i;
                }

            }

        }
    }

    @Override
    public String toString() {
        return "Max(" + getColumn().getName() + ")";
    }
}
