package org.laukvik.csv.statistics;

import java.math.BigDecimal;

/**
 * Counts all values based on a list of ranges.
 */
public class IntegerDistribution extends RangedDistribution<IntegerRange, Integer> {

    /**
     * The generated range size.
     */
    private int multiplier;

    /**
     * The value of ten.
     */
    private static final double TEN = 10d;

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
    public void buildRange(final Integer minimum, final Integer maximum) {
        getRanges().clear();
//        int min = Math.min(minimum, maximum);
//        int max = Math.max(minimum, maximum);
//
//        IntegerDistribution rd = new IntegerDistribution();
//        String valMin = "" + min;
//        String valMax = "" + max;
//        int decimalsMin = valMin.length();
//        int decimalsMax = valMax.length();
//
//        Double doubleMultiplier = Math.pow(TEN, decimalsMax - 1);
//        multiplier = doubleMultiplier.intValue();
//
//        for (int x= 0; x < COUNTS; x++){
//            int start = x * multiplier;
//            int end   = (start + multiplier) - 1;
//            rd.addRange(new IntegerRange(start + " .. " + end, start, end));
//        }
        int index = findPlace(new BigDecimal(maximum));

        if (index == 1) {
            multiplier = 1;
        } else if (index < 1) {
            multiplier = (int) Math.pow(TEN, (index * -1));
        } else {
            multiplier = (int) Math.pow(TEN, (index - 1));
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
    public Integer getRangeSize() {
        return multiplier;
    }

}
