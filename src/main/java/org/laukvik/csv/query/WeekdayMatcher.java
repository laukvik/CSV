package org.laukvik.csv.query;

import org.laukvik.csv.Row;
import org.laukvik.csv.columns.DateColumn;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Morten Laukvik
 */
public final class WeekdayMatcher extends RowMatcher implements ValueMatcher<Date> {

    /**
     * The value to compare.
     */
    private final List<Integer> values;
    /**
     * The column to compare.
     */
    private final DateColumn column;

    /**
     * The value of the column must be.
     *
     * @param dateColumn the column
     * @param value      the value
     */
    public WeekdayMatcher(final DateColumn dateColumn, final Integer... value) {
        this(dateColumn, Arrays.asList(value));
    }

    public WeekdayMatcher(final DateColumn dateColumn, final List<Integer> values) {
        this.column = dateColumn;
        this.values = values;
    }


    @Override
    public boolean matches(final Date value) {
        return values.contains(DateColumn.getDayOfWeek(value));
    }

    @Override
    public boolean matches(final Row row) {
        return matches(row.getDate(column));
    }
}
