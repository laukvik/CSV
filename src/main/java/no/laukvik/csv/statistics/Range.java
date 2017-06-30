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
    private final String label;
    /**
     * The minimum value.
     */
    private final T from;
    /**
     * The maximum value.
     */
    private final T to;
    /**
     * The amount of entries.
     */
    private int count;

    /**
     * Creates a new labelled range.
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
    public abstract void addValue(T value);

    /**
     * Checks whether the value is in this range.
     *
     * @param value the value
     * @return true when in this range
     */
    public abstract boolean contains(T value);

    /**
     * Returns the amount of values.
     *
     * @return the amount
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets the count.
     *
     * @param count the count
     */
    public void setCount(final int count) {
        this.count = count;
    }

    /**
     * Returns the label.
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns the from value.
     *
     * @return the from value
     */
    public T getFrom() {
        return from;
    }

    /**
     * Returns the to value.
     *
     * @return the to value
     */
    public T getTo() {
        return to;
    }
}
