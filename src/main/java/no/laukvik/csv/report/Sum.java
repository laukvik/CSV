package no.laukvik.csv.report;

import no.laukvik.csv.Row;
import no.laukvik.csv.columns.IntegerColumn;

import java.math.BigDecimal;

/**
 * Calculates the sumValue of all values.
 */
public final class Sum extends Aggregate {

    /**
     * Container for the sumValue.
     */
    private BigDecimal sumValue;

    /**
     * Creates a new SUM for the column.
     *
     * @param column the column
     */
    public Sum(final IntegerColumn column) {
        super(column);
        sumValue = new BigDecimal(0);
    }

    @Override
    public void aggregate(final Row row) {
        Object value = row.getObject(getColumn());
        if (value instanceof Integer) {
            Integer i = (Integer) value;

            if (sumValue == null) {
                sumValue = new BigDecimal(i);
            } else {
                sumValue = sumValue.add(new BigDecimal(i));
            }

        } else if (value instanceof Double) {
            Double i = (Double) value;

            if (sumValue == null) {
                sumValue = new BigDecimal(i);
            } else {
                sumValue = sumValue.add(new BigDecimal(i));
            }

        } else if (value instanceof Float) {
            Float i = (Float) value;

            if (sumValue == null) {
                sumValue = new BigDecimal(i);
            } else {
                sumValue = sumValue.add(new BigDecimal(i));
            }

        } else if (value instanceof BigDecimal) {
            BigDecimal i = (BigDecimal) value;

            if (sumValue == null) {
                sumValue = i;
            } else {
                sumValue = sumValue.add(i);
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
        return sumValue;
    }

    @Override
    public String toString() {
        return "Sum(" + getColumn().getName() + ")";
    }

}
