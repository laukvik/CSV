package org.laukvik.csv.fx;

import org.laukvik.csv.columns.Column;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents selected values in a column.
 */
public final class Selection {

    /**
     * The column.
     */
    private final Column column;
    /**
     * The selection values.
     */
    private final List<String> values;

    /**
     * Creates a new selection for the column.
     *
     * @param column the column
     */
    public Selection(final Column column) {
        this.column = column;
        this.values = new ArrayList<>();
    }

    /**
     * Returns the selected values.
     *
     * @return the values
     */
    public List<String> getValues() {
        return values;
    }

    /**
     * Returns the column.
     *
     * @return the column
     */
    public Column getColumn() {
        return column;
    }

    /**
     * Returns true if the selection is empty.
     *
     * @return true when selection is empty
     */
    public boolean isEmpty() {
        return values.isEmpty();
    }

    /**
     * Removes all selections.
     */
    public void clear() {
        this.values.clear();
    }

    /**
     * Adds a new selected value.
     *
     * @param value the value
     */
    public void addValue(final String value) {
        this.values.add(value);
    }

    /**
     * Removes a selected value.
     *
     * @param value the value
     */
    public void removeValue(final String value) {
        this.values.remove(value);
    }

    /**
     * Returns true if the object is equals to this.
     *
     * @param o the object
     * @return true when equals
     */
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Selection selection = (Selection) o;

        return getColumn() != null ? getColumn().equals(selection.getColumn()) : selection.getColumn() == null
                && (getValues() != null ? getValues().equals(selection.getValues()) : selection.getValues() == null);

    }

    /**
     * Returns the HashCode.
     *
     * @return the HashCode
     */
    public int hashCode() {
        int result = getColumn().hashCode();
        result = 31 * result + (getValues() != null ? getValues().hashCode() : 0);
        return result;
    }
}
