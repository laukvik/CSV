package org.laukvik.csv.statistics;

/**
 *
 *
 * @param <T> any number class
 */
public abstract class Range<T extends Number> {

    final String label;
    final T from;
    final T to;
    int count;

    public Range(final String label, final T from, final T to) {
        this.label = label;
        this.from = from;
        this.to = to;
        this.count = 0;
    }

    public abstract void addValue(final T value);

    public abstract boolean contains(final T value);

    public int getCount() {
        return count;
    }

    public String getLabel() {
        return label;
    }
}
