package no.laukvik.csv.query;

import no.laukvik.csv.columns.Column;
import no.laukvik.csv.columns.DateColumn;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Compares a DateColumn to have the specified weekday(s).
 */
public final class WeekdayMatcher implements ValueMatcher<Date> {

    /**
     * The value to compare.
     */
    private final List<Integer> values;
    /**
     * The column to compare.
     */
    private final DateColumn column;

    /**
     * Compares a DateColumn to have the specified weekday(s).
     *
     * @param dateColumn the column
     * @param value      the value
     */
    public WeekdayMatcher(final DateColumn dateColumn, final Integer... value) {
        this(dateColumn, Arrays.asList(value));
    }

    /**
     * Compares a DateColumn to have the specified weekday(s).
     *
     * @param dateColumn the column
     * @param values     the values
     */
    public WeekdayMatcher(final DateColumn dateColumn, final List<Integer> values) {
        this.column = dateColumn;
        this.values = values;
    }


    @Override
    public boolean matches(final Date value) {
        return values.contains(DateColumn.getDayOfWeek(value));
    }

    @Override
    public Column getColumn() {
        return column;
    }

}
