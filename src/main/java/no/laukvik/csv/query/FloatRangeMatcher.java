package no.laukvik.csv.query;

import no.laukvik.csv.columns.Column;
import no.laukvik.csv.columns.FloatColumn;
import no.laukvik.csv.statistics.FloatRange;

import java.util.Arrays;
import java.util.List;

/**
 * Compares a float value to a list of FloatRanges.
 */
public final class FloatRangeMatcher implements ValueMatcher<Float> {

    /**
     * The FloatColumn to match.
     */
    private final FloatColumn column;

    /**
     * The list of ranges.
     */
    private final List<FloatRange> ranges;

    /**
     * Creates a new instance with the specified floatColumn and range(s).
     *
     * @param floatColumn the column
     * @param range       the ranges
     */
    public FloatRangeMatcher(final FloatColumn floatColumn, final FloatRange... range) {
        this(floatColumn, Arrays.asList(range));
    }

    /**
     * Creates a new instance with the specified integerColumn and range(s).
     *
     * @param floatColumn the column
     * @param range       the ranges
     */
    public FloatRangeMatcher(final FloatColumn floatColumn, final List<FloatRange> range) {
        this.column = floatColumn;
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
    public boolean matches(final Float value) {
        for (FloatRange r : ranges) {
            if (r.contains(value)) {
                return true;
            }
        }
        return false;
    }
}
