package org.laukvik.csv.query;

import org.laukvik.csv.Row;
import org.laukvik.csv.columns.DateColumn;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 *
 */
public final class DateOfMonthMatcher extends RowMatcher implements ValueMatcher<Date> {

    /**
     * The value to compare.
     */
    private final List<Integer> values;
    /**
     * The column to compare.
     */
    private final DateColumn column;

    /**
     * The date of the column must be among the values.
     *
     * @param dateColumn the column
     * @param value      the value
     */
    public DateOfMonthMatcher(final DateColumn dateColumn, final Integer... value) {
        this(dateColumn, Arrays.asList(value));
    }

    /**
     * The date of the column must be among the values.
     *
     * @param dateColumn the column
     * @param values      the value
     */
    public DateOfMonthMatcher(final DateColumn dateColumn, final List<Integer> values) {
        super();
        this.column = dateColumn;
        this.values = values;
    }

    /**
     * Returns true when matches the value.
     * @param value the value to test against
     * @return true when matches
     */
    public boolean matches(final Date value) {
        return values.contains(DateColumn.getDayOfMonth(value));
    }

    /**
     * Returns true when matches the value.
     * @param row the value to test against
     * @return true when matches
     */
    public boolean matches(final Row row) {
        return matches(row.getDate(column));
    }
}
