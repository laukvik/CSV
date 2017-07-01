package no.laukvik.csv.statistics;

import java.util.ArrayList;
import java.util.List;

/**
 * Counts all values in each range.
 *
 * @param <T> the range type
 * @param <V> the number type
 */
public abstract class RangedDistribution<T extends Range, V extends Number> {

    /**
     * The list of ranges.
     */
    private final List<T> ranges;

    /**
     * The amount of nulls.
     */
    private int nullCount;

    /**
     * Creates an empty instance.
     */
    public RangedDistribution() {
        this.ranges = new ArrayList<>();
        nullCount = 0;
    }

    /**
     * Adds a new IntegerRange.
     *
     * @param range the IntegerRange
     */
    public void addRange(final T range) {
        this.ranges.add(range);
    }

    /**
     * Returns a list of all ranges.
     *
     * @return all ranges
     */
    public List<T> getRanges() {
        return ranges;
    }

    /**
     * Returns the amount of null values.
     *
     * @return the amount of null values
     */
    public int getNullCount() {
        return nullCount;
    }

    /**
     * Counts the value if its in the specified ranges.
     *
     * @param value the value
     */
    public void addValue(final V value) {
        if (value == null) {
            nullCount++;
        } else {
            for (T r : ranges) {
                r.addValue(value);
            }
        }
    }

}
