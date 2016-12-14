package org.laukvik.csv.report;

import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;
import org.laukvik.csv.columns.IntegerColumn;

/**
 * An abstract class that aggregates a column.
 */
public abstract class Aggregate {

    /**
     * The column it aggregates.
     */
    private Column c;
    /**
     * The new column with the aggregated data.
     */
    private IntegerColumn aggregateColumn;

    /**
     * Creates a new instance with the column.
     * @param column the column
     */
    public Aggregate(final Column column) {
        this.c = column;
    }

    /**
     * Returns the column it aggregates
     * @return the column
     */
    public Column getColumn() {
        return c;
    }

    /**
     * Aggregate the column in the row
     * @param row the row
     */
    public abstract void aggregate(Row row);

    /**
     * Returns the aggregated value
     * @return the aggregated value
     */
    public abstract Object getValue();


    public abstract void reset();

    public void setAggregateColumn(IntegerColumn aggregateColumn) {
        this.aggregateColumn = aggregateColumn;
    }

    public IntegerColumn getAggregateColumn() {
        return aggregateColumn;
    }
}
