package org.laukvik.csv.statistics;

import java.math.BigDecimal;
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
     * Constant for the number 10.
     */
    public static final int TEN = 10;

    /**
     *
     */
    public static final int COUNTS = 10;

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
     * Returns the index of the first non-zero digit in the value. Trailing zeros
     * are ignored. Negative index is the position to the right of the period while
     * positive index is to the left of the period.
     *
     * @param value the value
     * @return the index
     */
    public static int findPlace(final BigDecimal value) {
        BigDecimal bd = value.stripTrailingZeros();
        final String strippedValues = bd.toPlainString();
//        int index;
        if (strippedValues.contains(".")) {
            String[] arr = strippedValues.split("\\.");
            String left = arr[0];
            String right = arr[1];
            if (left.equals("0")) {
                // 0.xxx
//                index = right.indexOf("^[1-9]*");

                for (int x = 0; x < right.length(); x++) {
                    if (right.charAt(x) != 48) {
//                        x = right.length(); // exit loop
                        return (x * -1) - 1;
                    }
                }
                return right.length();
            } else {
                // 1.xx >
//                index = left.length();
                return left.length();
            }
        } else {
//            index = strippedValues.length();
            return strippedValues.length();
        }
//        return index;
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

    /**
     * Builds a new distribution based on minimum and maximum values.
     *
     * @param minimum the minimum value
     * @param maximum the maximum value
     */
    public abstract void buildRange(V minimum, V maximum);

    /**
     * Returns the interval size for each range.
     *
     * @return the interval size for each range.
     */
    public abstract V getRangeSize();

}
