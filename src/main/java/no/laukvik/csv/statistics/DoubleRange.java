package no.laukvik.csv.statistics;

/**
 * Identifies a range of Doubles by specifying the minimum and maximum values for the range.
 */
public final class DoubleRange extends Range<Double> {

    /**
     * Specifies a labelled range of Doubles between from and to.
     *
     * @param label the label
     * @param min   the minimum value
     * @param max   the maximum value
     */
    public DoubleRange(final String label, final Double min, final Double max) {
        super(label, min, max);
    }

    /**
     * Adds a value.
     *
     * @param value the value
     */
    @Override
    public void addValue(final Double value) {
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
    public boolean contains(final Double value) {
        if (value == null) {
            return false;
        }
        return value >= from && value < to;
    }

}
