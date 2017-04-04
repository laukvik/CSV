package no.laukvik.csv.statistics;

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

}
