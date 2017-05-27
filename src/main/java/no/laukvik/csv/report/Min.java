package no.laukvik.csv.report;

import no.laukvik.csv.Row;
import no.laukvik.csv.columns.Column;

import java.math.BigDecimal;

/**
 * Calculates the minimum of all values.
 */
public final class Min extends Aggregate {

    /**
     * Container for the minimum value.
     */
    private BigDecimal min;

    /**
     * Creates a new Min with the column.
     *
     * @param column the column
     */
    public Min(final Column column) {
        super(column);
    }


    /**
     * Returns the value.
     *
     * @return the value
     */
    @Override
    public BigDecimal getValue() {
        return min;
    }

    /**
     * Aggregates the value in the row.
     *
     * @param row the row
     */
    @Override
    public void aggregate(final Row row) {
        Object value = row.getObject(getColumn());
        if (value != null) {

            if (value instanceof Integer) {
                Integer i = (Integer) value;

                if (min == null) {
                    min = new BigDecimal(i);
                } else if (i < min.intValue()) {
                    min = new BigDecimal(i);
                }

            } else if (value instanceof Double) {
                Double i = (Double) value;

                if (min == null) {
                    min = new BigDecimal(i);
                } else if (i < min.doubleValue()) {
                    min = new BigDecimal(i);
                }

            } else if (value instanceof Float) {
                Float i = (Float) value;

                if (min == null) {
                    min = new BigDecimal(i);
                } else if (i < min.floatValue()) {
                    min = new BigDecimal(i);
                }

            } else if (value instanceof BigDecimal) {
                BigDecimal i = (BigDecimal) value;

                if (min == null) {
                    min = i;
                } else if (0 < min.compareTo(i)) {
                    min = i;
                }

            }

        }
    }

    @Override
    public String toString() {
        return "Min(" + getColumn().getName() + ")";
    }
}
