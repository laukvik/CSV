package org.laukvik.csv.statistics;

/**
 * Counts all values based on a list of ranges.
 */
public final class FloatDistribution extends RangedDistribution<FloatRange, Float> {

    /**
     * The generated range size.
     */
    private float multiplier;

    /**
     * Creates an empty instance.
     */
    public FloatDistribution() {
        super();
    }

    /**
     * Builds a new IntegerDistribution based on the minimum and maximum values.
     *
     * @param minimum the minimum value
     * @param maximum the maximum value
     */
    public void buildRange(final Float minimum, final Float maximum) {
        getRanges().clear();
        float max = Math.max(minimum, maximum);
        String valMax = Float.toString(max);
        int decimalsMax = valMax.length();
        Double doubleMultiplier = Math.pow(TEN, decimalsMax - 1);
        multiplier = doubleMultiplier.floatValue();
        for (int x = 0; x < COUNTS; x++) {
            float start = x * multiplier;
            float end = (start + multiplier) - 1;
            addRange(new FloatRange(start + " .. " + end, start, end));
        }
    }

    /**
     * Returns the interval size for each range.
     *
     * @return the interval size for each range.
     */
    public Float getRangeSize() {
        return multiplier;
    }

}
