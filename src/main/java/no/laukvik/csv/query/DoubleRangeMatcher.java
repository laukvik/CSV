package no.laukvik.csv.query;

import no.laukvik.csv.columns.Column;
import no.laukvik.csv.columns.DoubleColumn;
import no.laukvik.csv.statistics.DoubleRange;

import java.util.Arrays;
import java.util.List;

/**
 * Compares a double value to a list of DoubleRanges.
 */
public final class DoubleRangeMatcher implements ValueMatcher<Double> {

    /**
     * The DoubleColumn to match.
     */
    private final DoubleColumn column;

    /**
     * The list of ranges.
     */
    private final List<DoubleRange> ranges;

    /**
     * Creates a new instance with the specified doubleColumn and range(s).
     *
     * @param doubleColumn the column
     * @param range        the ranges
     */
    public DoubleRangeMatcher(final DoubleColumn doubleColumn, final DoubleRange... range) {
        this(doubleColumn, Arrays.asList(range));
    }

    /**
     * Creates a new instance with the specified doubleColumn and range(s).
     *
     * @param doubleColumn the column
     * @param range        the ranges
     */
    public DoubleRangeMatcher(final DoubleColumn doubleColumn, final List<DoubleRange> range) {
        super();
        this.column = doubleColumn;
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
    public boolean matches(final Double value) {
        for (DoubleRange r : ranges) {
            if (r.contains(value)) {
                return true;
            }
        }
        return false;
    }
}
