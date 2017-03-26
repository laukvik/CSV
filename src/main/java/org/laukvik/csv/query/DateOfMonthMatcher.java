package org.laukvik.csv.query;

import org.laukvik.csv.columns.Column;
import org.laukvik.csv.columns.DateColumn;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Compares a DateColumn to have the specified date of month.
 *
 */
public final class DateOfMonthMatcher implements ValueMatcher<Date> {

    /**
     * The value to compare.
     */
    private final List<Integer> values;
    /**
     * The column to compare.
     */
    private final DateColumn column;

    /**
     * Compares a DateColumn to have the specified date of month.
     *
     * @param dateColumn the column
     * @param value      the value
     */
    public DateOfMonthMatcher(final DateColumn dateColumn, final Integer... value) {
        this(dateColumn, Arrays.asList(value));
    }

    /**
     * Compares a DateColumn to have the specified date of month.
     *
     * @param dateColumn the column
     * @param values      the value
     */
    public DateOfMonthMatcher(final DateColumn dateColumn, final List<Integer> values) {
        super();
        this.column = dateColumn;
        this.values = values;
    }

    @Override
    public Column getColumn() {
        return column;
    }

    /**
     * Returns true when matchesRow the value.
     * @param value the value to test against
     * @return true when matchesRow
     */
    public boolean matches(final Date value) {
        return values.contains(DateColumn.getDayOfMonth(value));
    }

}
