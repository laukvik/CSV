package org.laukvik.csv.fx;

import org.laukvik.csv.columns.Column;

/**
 * @author Morten Laukvik
 */
public class Selection {

    private final Column column;
    private String value;

    public Selection(final Column column, final String value) {
        this.column = column;
        this.value = value;
    }

    public Column getColumn() {
        return column;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    @Override
    public boolean equals(final Object o) {
        Selection other = (Selection) o;
        return column.equals(other.getColumn()) && value.equals(other.getValue());
    }

    @Override
    public int hashCode() {
        int result = getColumn() != null ? getColumn().hashCode() : 0;
        result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
        return result;
    }
}
