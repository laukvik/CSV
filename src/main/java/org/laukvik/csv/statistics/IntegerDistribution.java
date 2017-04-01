package org.laukvik.csv.statistics;

import java.math.BigDecimal;

/**
 * Counts all values based on a list of ranges.
 */
public final class IntegerDistribution extends RangedDistribution<IntegerRange, Integer> {

    /**
     * The value of ten.
     */
    private static final double TEN = 10d;
    /**
     * The generated range size.
     */
    private int multiplier;

    /**
     * Creates an empty instance.
     */
    public IntegerDistribution() {
        super();
    }

    /**
     * Builds a new IntegerDistribution based on the minimum and maximum values.
     *
     * @param minimum the minimum value
     * @param maximum the maximum value
     */
    @Override
    public void buildRange(final Integer minimum, final Integer maximum) {
        getRanges().clear();
        int index = findPlace(new BigDecimal(maximum));

        if (index == 1) {
            multiplier = 1;
        } else if (index < 1) {
            multiplier = (int) Math.pow(TEN, index * -1);
        } else {
            multiplier = (int) Math.pow(TEN, index - 1);
        }

        for (int x = 0; x < COUNTS; x++) {
            int start = x * multiplier;
            int end = (x + 1) * multiplier;
            addRange(new IntegerRange(start + " .. " + end, start, end));
        }
    }

    /**
     * Returns the interval size for each range.
     *
     * @return the interval size for each range.
     */
    @Override
    public Integer getRangeSize() {
        return multiplier;
    }

}
