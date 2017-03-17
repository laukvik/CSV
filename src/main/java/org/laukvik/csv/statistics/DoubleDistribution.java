package org.laukvik.csv.statistics;

/**
 * Counts all values based on a list of ranges.
 *
 */
public class DoubleDistribution extends RangedDistribution<DoubleRange, Double>{

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
    public void buildRange(final Double minimum, final Double maximum){
        getRanges().clear();
        Double max = Math.max(minimum, maximum);

        String valMax = "" + max.intValue();
        int decimalsMax = valMax.length();

        doubleMultiplier = Math.pow(TEN, decimalsMax - 1);

        if (max > 0 && max < 1){
            doubleMultiplier = 0.1;
        }

        for (int x= 0; x < COUNTS; x++){
            double start = x * doubleMultiplier;
            double end   = (start + doubleMultiplier) - doubleMultiplier;
            addRange(new DoubleRange(start + " .. " + end, start, end));
        }
    }

    /**
     * Returns the interval size for each range.
     *
     * @return the interval size for each range.
     */
    public Double getRangeSize() {
        return doubleMultiplier;
    }

}
