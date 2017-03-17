package org.laukvik.csv.query;

import org.laukvik.csv.Row;
import org.laukvik.csv.columns.IntegerColumn;
import org.laukvik.csv.statistics.IntegerRange;
import java.util.Arrays;
import java.util.List;

/**
 * Matches Integers in the specified ranges.
 *
 */
public class IntegerRangeMatcher extends RowMatcher implements ValueMatcher<Integer>{

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
        super();
        this.column = integerColumn;
        this.ranges = range;
    }

    /**
     * Returns true when the row matches.
     *
     * @param row the row
     * @return true when the row matches
     */
    public boolean matches(final Row row) {
        Integer i = row.getInteger(column);
        return matches(i);
    }

    /**
     * Returns true when the value matches.
     *
     * @param value the value to test against
     * @return true when matches
     */
    @Override
    public boolean matches(final Integer value) {
        for (IntegerRange r : ranges){
            if (r.contains(value)){
                return true;
            }
        }
        return false;
    }
}
