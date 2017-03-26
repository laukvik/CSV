package org.laukvik.csv.query;

import org.laukvik.csv.Row;
import org.laukvik.csv.columns.BigDecimalColumn;
import org.laukvik.csv.columns.Column;
import org.laukvik.csv.statistics.BigDecimalRange;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Matches BigDecimal in the specified ranges.
 */
public final class BigDecimalRangeMatcher implements ValueMatcher<BigDecimal> {

    /**
     * The BigDecimalColumn to match.
     */
    private final BigDecimalColumn column;

    /**
     * The list of ranges.
     */
    private final List<BigDecimalRange> ranges;

    /**
     * Creates a new instance with the specified BigDecimalColumn and range(s).
     *
     * @param bigDecimalColumn the column
     * @param range            the ranges
     */
    public BigDecimalRangeMatcher(final BigDecimalColumn bigDecimalColumn, final BigDecimalRange... range) {
        this(bigDecimalColumn, Arrays.asList(range));
    }

    /**
     * Creates a new instance with the specified bigDecimalColumn and range(s).
     *
     * @param bigDecimalColumn the column
     * @param range            the ranges
     */
    public BigDecimalRangeMatcher(final BigDecimalColumn bigDecimalColumn, final List<BigDecimalRange> range) {
        super();
        this.column = bigDecimalColumn;
        this.ranges = range;
    }

    @Override
    public Column getColumn() {
        return column;
    }

    /**
     * Returns true when the row matchesRow.
     *
     * @param row the row
     * @return true when the row matchesRow
     */
    public boolean matches(final Row row) {
        BigDecimal i = row.getBigDecimal(column);
        return matches(i);
    }

    /**
     * Returns true when the value matchesRow.
     *
     * @param value the value to test against
     * @return true when matchesRow
     */
    @Override
    public boolean matches(final BigDecimal value) {
        for (BigDecimalRange r : ranges) {
            if (r.contains(value)) {
                return true;
            }
        }
        return false;
    }
}
