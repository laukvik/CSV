package org.laukvik.csv.query;

import org.laukvik.csv.columns.DateColumn;

import java.util.Date;

/**
 * An abstract class that has all utility methods to be helpful.
 *
 */
public abstract class AbstractDateMatcher extends RowMatcher {

    /**
     * The date.
     */
    private final Date value;
    /**
     * The DateColumn.
     */
    private final DateColumn column;

    /**
     * Creates a new matcher.
     *
     * @param dateColumn the dateColumn
     * @param date      the date
     */
    AbstractDateMatcher(final DateColumn dateColumn, final Date date) {
        super();
        this.column = dateColumn;
        this.value = date;
    }

    /**
     * Returns the date.
     *
     * @return the date
     */
    public final Date getValue() {
        return value;
    }

    /**
     * Returns the column.
     *
     * @return the column
     */
    public final DateColumn getColumn() {
        return column;
    }
}
