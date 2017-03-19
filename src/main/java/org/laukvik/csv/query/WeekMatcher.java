package org.laukvik.csv.query;

import org.laukvik.csv.Row;
import org.laukvik.csv.columns.DateColumn;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Morten Laukvik
 */
public final class WeekMatcher extends RowMatcher implements ValueMatcher<Date> {

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
    public WeekMatcher(final DateColumn dateColumn, final Integer... value) {
        this(dateColumn, Arrays.asList(value));
    }

    public WeekMatcher(final DateColumn dateColumn, final List<Integer> values) {
        super();
        this.column = dateColumn;
        this.values = values;
    }

    public boolean matches(final Date value) {
        Integer year = DateColumn.getWeekOfYear(value);
        for (Integer v : values) {
            if (year == null) {
                if (v == null) {
                    return true;
                }
            } else if (year.equals(v)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean matches(final Row row) {
        Date date = row.getDate(column);
        return matches(date);
    }
}
