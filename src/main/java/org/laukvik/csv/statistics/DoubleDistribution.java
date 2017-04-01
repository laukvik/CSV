package org.laukvik.csv.statistics;

/**
 * Counts all values based on a list of ranges.
 */
public final class DoubleDistribution extends RangedDistribution<DoubleRange, Double> {

    /**
     * A tenth.
     */
    private static final double TENTH = 0.1d;
    /**
     * The generated range size.
     */
    private double doubleMultiplier;

    /**
     * Creates an empty instance.
     */
    public DoubleDistribution() {
        super();
    }

    /**
     * Builds a new IntegerDistribution based on the minimum and maximum values.
     *
     * @param minimum the minimum value
     * @param maximum the maximum value
     */
    @Override
    public void buildRange(final Double minimum, final Double maximum) {
        getRanges().clear();
        Double max = Math.max(minimum, maximum);

        String valMax = Double.toString(max);

        int decimalsMax = valMax.length();

        doubleMultiplier = Math.pow(TEN, decimalsMax - 1);

        if (max > 0 && max < 1) {
            doubleMultiplier = TENTH;
        }

        for (int x = 0; x < COUNTS; x++) {
            double start = x * doubleMultiplier;
            double end = (start + doubleMultiplier) - doubleMultiplier;
            addRange(new DoubleRange(start + " .. " + end, start, end));
        }
    }

    /**
     * Returns the interval size for each range.
     *
     * @return the interval size for each range.
     */
    @Override
    public Double getRangeSize() {
        return doubleMultiplier;
    }

}
