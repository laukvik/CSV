package no.laukvik.csv.query;

import no.laukvik.csv.columns.Column;
import no.laukvik.csv.columns.DateColumn;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Compares a DateColumn to have the specified month(s).
 */
public final class WeekMatcher implements ValueMatcher<Date> {

    /**
     * The value to compare.
     */
    private final List<Integer> values;
    /**
     * The column to compare.
     */
    private final DateColumn column;

    /**
     * Compares a DateColumn to have the specified month(s).
     *
     * @param dateColumn the column
     * @param value      the value
     */
    public WeekMatcher(final DateColumn dateColumn, final Integer... value) {
        this(dateColumn, Arrays.asList(value));
    }

    /**
     * Compares a DateColumn to have the specified month(s).
     *
     * @param dateColumn the column
     * @param values     the value
     */
    public WeekMatcher(final DateColumn dateColumn, final List<Integer> values) {
        this.column = dateColumn;
        this.values = values;
    }

    @Override
    public Column getColumn() {
        return column;
    }

    @Override
    public boolean matches(final Date value) {
        return values.contains(DateColumn.getWeekOfYear(value));
    }

}
