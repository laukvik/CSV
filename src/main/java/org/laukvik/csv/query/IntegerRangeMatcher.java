package org.laukvik.csv.query;

import org.laukvik.csv.columns.Column;
import org.laukvik.csv.columns.IntegerColumn;
import org.laukvik.csv.statistics.IntegerRange;

import java.util.Arrays;
import java.util.List;

/**
 * Compares an Integer value to a list of IntegerRanges.
 */
public final class IntegerRangeMatcher implements ValueMatcher<Integer> {

    /**
     * The IntegerColumn to match.
     */
    private final IntegerColumn column;

    /**
     * The list of ranges.
     */
    private final List<IntegerRange> ranges;

    /**
     * Creates a new instance with the specified integerColumn and range(s).
     *
     * @param integerColumn the column
     * @param range         the ranges
     */
    public IntegerRangeMatcher(final IntegerColumn integerColumn, final IntegerRange... range) {
        this(integerColumn, Arrays.asList(range));
    }

    /**
     * Creates a new instance with the specified integerColumn and range(s).
     *
     * @param integerColumn the column
     * @param range         the ranges
     */
    public IntegerRangeMatcher(final IntegerColumn integerColumn, final List<IntegerRange> range) {
        this.column = integerColumn;
        this.ranges = range;
    }

    @Override
    public Column getColumn() {
        return column;
    }

    /**
     * Returns true when the value matchesRow.
     *
     * @param value the value to test against
     * @return true when matchesRow
     */
    @Override
    public boolean matches(final Integer value) {
        for (IntegerRange r : ranges) {
            if (r.contains(value)) {
                return true;
            }
        }
        return false;
    }
}
