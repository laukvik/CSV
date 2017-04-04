package no.laukvik.csv.statistics;

/**
 * Identifies a range of Integers by specifying the minimum and maximum values for the range.
 */
public final class IntegerRange extends Range<Integer> {

    /**
     * Specifies a labelled range of Integers between from and to.
     *
     * @param label the label
     * @param min   the minimum value
     * @param max   the maximum value
     */
    public IntegerRange(final String label, final Integer min, final Integer max) {
        super(label, min, max);
    }

    /**
     * Adds a value.
     *
     * @param value a value
     */
    @Override
    public void addValue(final Integer value) {
        if (contains(value)) {
            count++;
        }
    }

    /**
     * Returns true when the value is within the range.
     *
     * @param value the value
     * @return when the value is within the range
     */
    @Override
    public boolean contains(final Integer value) {
        if (value == null) {
            return false;
        }
        return value >= from && value < to;
    }

}
