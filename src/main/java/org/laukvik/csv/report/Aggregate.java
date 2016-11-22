package org.laukvik.csv.report;

import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;

/**
 *
 */
public abstract class Aggregate {

    private Column c;

    public Aggregate(final Column column) {
        this.c = column;
    }

    public Column getColumn() {
        return c;
    }

    public abstract void aggregate(final Row row);

    public abstract Object getValue();

}
