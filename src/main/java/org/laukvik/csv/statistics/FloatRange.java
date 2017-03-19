package org.laukvik.csv.statistics;

/**
 * Identifies a range of Floats by specifying the minimum and maximum values for the range.
 */
public final class FloatRange extends Range<Float> {

    /**
     * Specifies a labelled range of Floats between from and to.
     *
     * @param label the label
     * @param min   the minimum value
     * @param max   the maximum value
     */
    public FloatRange(final String label, final Float min, final Float max) {
        super(label, min, max);
    }

    /**
     * Adds a value.
     *
     * @param value the value
     */
    @Override
    public void addValue(final Float value) {
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
    public boolean contains(final Float value) {
        if (value == null) {
            return false;
        }
        return value >= from && value <= to;
    }

}
