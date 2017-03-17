package org.laukvik.csv.query;

import org.laukvik.csv.Row;
import org.laukvik.csv.columns.FloatColumn;
import org.laukvik.csv.statistics.FloatRange;

import java.util.Arrays;
import java.util.List;

/**
 * Matches Floats in the specified ranges.
 *
 */
public class FloatRangeMatcher extends RowMatcher implements ValueMatcher<Float>{

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
     * @param range         the ranges
     */
    public FloatRangeMatcher(final FloatColumn floatColumn, final FloatRange... range) {
        this(floatColumn, Arrays.asList(range));
    }

    /**
     * Creates a new instance with the specified integerColumn and range(s).
     *
     * @param floatColumn the column
     * @param range         the ranges
     */
    public FloatRangeMatcher(final FloatColumn floatColumn, final List<FloatRange> range) {
        super();
        this.column = floatColumn;
        this.ranges = range;
    }

    /**
     * Returns true when the row matches.
     *
     * @param row the row
     * @return true when the row matches
     */
    public boolean matches(final Row row) {
        Float i = row.getFloat(column);
        return matches(i);
    }

    /**
     * Returns true when the value matches.
     *
     * @param value the value to test against
     * @return true when matches
     */
    @Override
    public boolean matches(final Float value) {
        for (FloatRange r : ranges){
            if (r.contains(value)){
                return true;
            }
        }
        return false;
    }
}
