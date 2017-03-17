package org.laukvik.csv.query;

import org.laukvik.csv.Row;
import org.laukvik.csv.columns.DoubleColumn;
import org.laukvik.csv.statistics.DoubleRange;

import java.util.Arrays;
import java.util.List;

/**
 * Matches Floats in the specified ranges.
 *
 */
public class DoubleRangeMatcher extends RowMatcher implements ValueMatcher<Double>{

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
     * @param range         the ranges
     */
    public DoubleRangeMatcher(final DoubleColumn doubleColumn, final DoubleRange... range) {
        this(doubleColumn, Arrays.asList(range));
    }

    /**
     * Creates a new instance with the specified doubleColumn and range(s).
     *
     * @param doubleColumn the column
     * @param range         the ranges
     */
    public DoubleRangeMatcher(final DoubleColumn doubleColumn, final List<DoubleRange> range) {
        super();
        this.column = doubleColumn;
        this.ranges = range;
    }

    /**
     * Returns true when the row matches.
     *
     * @param row the row
     * @return true when the row matches
     */
    public boolean matches(final Row row) {
        Double i = row.getDouble(column);
        return matches(i);
    }

    /**
     * Returns true when the value matches.
     *
     * @param value the value to test against
     * @return true when matches
     */
    @Override
    public boolean matches(final Double value) {
        for (DoubleRange r : ranges){
            if (r.contains(value)){
                return true;
            }
        }
        return false;
    }
}
