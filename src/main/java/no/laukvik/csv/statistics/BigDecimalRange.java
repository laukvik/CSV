package no.laukvik.csv.statistics;

import java.math.BigDecimal;

/**
 * Identifies a range of BigDecimals by specifying the minimum and maximum values for the range.
 */
public final class BigDecimalRange extends Range<BigDecimal> {

    /**
     * Specifies a labelled range of BigDecimals between from and to.
     *
     * @param label the label
     * @param min   the minimum value
     * @param max   the maximum value
     */
    public BigDecimalRange(final String label, final BigDecimal min, final BigDecimal max) {
        super(label, min, max);
    }

    /**
     * Adds a value.
     *
     * @param value the value
     */
    @Override
    public void addValue(final BigDecimal value) {
        if (contains(value)) {
            setCount(getCount() + 1);
        }
    }

    /**
     * Returns true when the value is within the range.
     *
     * @param value the value
     * @return when the value is within the range
     */
    @Override
    public boolean contains(final BigDecimal value) {
        if (value == null) {
            return false;
        }
        return value.compareTo(getFrom()) >= 0 && value.compareTo(getTo()) < 0;
    }

}
