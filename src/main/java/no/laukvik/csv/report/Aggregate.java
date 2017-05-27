package no.laukvik.csv.report;

import no.laukvik.csv.Row;
import no.laukvik.csv.columns.Column;

/**
 * An abstract class that aggregates a column.
 */
public abstract class Aggregate {

    /**
     * The column it aggregates.
     */
    private final Column c;
    /**
     * The new column with the aggregated data.
     */
    private Column aggregateColumn;

    /**
     * Creates a new instance with the column.
     *
     * @param column the column
     */
    public Aggregate(final Column column) {
        this.c = column;
    }

    /**
     * Returns the column it aggregates.
     *
     * @return the column
     */
    public Column getColumn() {
        return c;
    }

    /**
     * Aggregate the column in the row.
     *
     * @param row the row
     */
    public abstract void aggregate(Row row);

    /**
     * Returns the aggregated value.
     *
     * @return the aggregated value
     */
    public abstract Object getValue();

    /**
     * Returns the column to aggregate.
     *
     * @return the column
     */
    public Column getAggregateColumn() {
        return aggregateColumn;
    }

    /**
     * Sets the column to aggregate.
     *
     * @param aggregateColumn the column
     */
    public void setAggregateColumn(final Column aggregateColumn) {
        this.aggregateColumn = aggregateColumn;
    }


}
