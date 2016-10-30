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
    protected Date value;
    /**
     * The DateColumn.
     */
    protected DateColumn column;

    /**
     * Creats a new matcher.
     *
     * @param dateColumn the dateColumn
     * @param date      the date
     */
    AbstractDateMatcher(final DateColumn dateColumn, final Date date) {
        super();
        this.column = dateColumn;
        this.value = date;
    }

}
