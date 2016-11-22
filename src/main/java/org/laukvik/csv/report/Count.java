package org.laukvik.csv.report;

import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;

/**
 * Counts all items.
 *
 */
public final class Count extends Aggregate {

    private long count;

    public Count(final Column column) {
        super(column);
        count = 0;
    }

    public void aggregate(final Row row) {
        count++;
    }

    public Long getValue() {
        return count;
    }

}
