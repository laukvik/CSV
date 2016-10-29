package org.laukvik.csv.fx;

import org.laukvik.csv.columns.Column;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Morten Laukvik
 */
public class Selection {

    private final Column column;
    private final List<String> values;

    public Selection(final Column column) {
        this.column = column;
        this.values = new ArrayList<>();
    }

    public List<String> getValues() {
        return values;
    }

    public Column getColumn() {
        return column;
    }

    public boolean isEmpty(){
        return values.isEmpty();
    }

    public void clear() {
        this.values.clear();
    }

    public void addValue(final String value) {
        this.values.add(value);
    }

    public void removeValue(final String value) {
        this.values.remove(value);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Selection selection = (Selection) o;

        return getColumn() != null ? getColumn().equals(selection.getColumn()) : selection.getColumn() == null && (getValues() != null ? getValues().equals(selection.getValues()) : selection.getValues() == null);

    }

    @Override
    public int hashCode() {
        int result = getColumn().hashCode();
        result = 31 * result + (getValues() != null ? getValues().hashCode() : 0);
        return result;
    }
}
