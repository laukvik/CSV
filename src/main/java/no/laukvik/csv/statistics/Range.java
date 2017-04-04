package no.laukvik.csv.statistics;

/**
 * An abstract class for defining a labelled range.
 *
 * @param <T> any number class
 */
public abstract class Range<T extends Number> {

    /**
     * The label.
     */
    final String label;
    /**
     * The minimum value.
     */
    final T from;
    /**
     * The maximum value.
     */
    final T to;
    /**
     * The amount of entries.
     */
    int count;

    /**
     * Creates a new labelled range
     *
     * @param label the label
     * @param from  the minimum value
     * @param to    the maximum value (exclusive)
     */
    public Range(final String label, final T from, final T to) {
        this.label = label;
        this.from = from;
        this.to = to;
        this.count = 0;
    }

    /**
     * Counts this entry if its inside the range.
     *
     * @param value the value
     */
    public abstract void addValue(final T value);

    /**
     * Checks whether the value is in this range.
     *
     * @param value the value
     * @return true when in this range
     */
    public abstract boolean contains(final T value);

    /**
     * Returns the amount of values.
     *
     * @return the amount
     */
    public int getCount() {
        return count;
    }

    /**
     * Returns the label.
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }
}
