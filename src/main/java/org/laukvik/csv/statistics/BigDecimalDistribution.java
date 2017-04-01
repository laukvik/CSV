package org.laukvik.csv.statistics;

import java.math.BigDecimal;

/**
 * Counts all values based on a list of ranges.
 */
public final class BigDecimalDistribution extends RangedDistribution<BigDecimalRange, BigDecimal> {

    /**
     * The generated range size.
     */
    private BigDecimal multiplier;

    /**
     * Creates an empty instance.
     */
    public BigDecimalDistribution() {
        super();
    }

    /**
     * Builds a new IntegerDistribution based on the minimum and maximum values.
     *
     * @param min the minimum value
     * @param max the maximum value
     */
    @Override
    public void buildRange(final BigDecimal min, final BigDecimal max) {
        getRanges().clear();
        BigDecimal ten = new BigDecimal(TEN);

        int index = findPlace(max);



        if (index == 1) {
            multiplier = BigDecimal.valueOf(1);
        } else if (index < 1) {
            multiplier = BigDecimal.valueOf(1f).divide(ten.pow(index * -1));
        } else {
            multiplier = BigDecimal.valueOf(1f).pow(index);
        }

        for (int x = 0; x < COUNTS; x++) {
            BigDecimal start = new BigDecimal(x).multiply(multiplier);
            BigDecimal end = new BigDecimal((x + 1)).multiply(multiplier);
            addRange(new BigDecimalRange(start + " .. " + end, start, end));
        }
    }

    /**
     * Returns the interval size for each range.
     *
     * @return the size
     */
    @Override
    public BigDecimal getRangeSize() {
        return multiplier;
    }

}
