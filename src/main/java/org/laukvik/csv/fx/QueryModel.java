package org.laukvik.csv.fx;

import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;
import org.laukvik.csv.columns.StringColumn;
import org.laukvik.csv.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores a selection of multiple columns and values in a flat structure.
 *
 * @author Morten Laukvik
 */
public final class QueryModel {

    /**
     * The Main instance.
     */
    private final Main main;
    /**
     * The CSV instance.
     */
    private final CSV csv;
    /** The Selection of values for the query. */
    private final List<Selection> selections;

    /**
     * Creates a new instance.
     * @param csv the csv instance
     * @param main the main instance
     */
    public QueryModel(final CSV csv, final Main main) {
        this.csv = csv;
        this.main = main;
        this.selections = new ArrayList<>();
    }

    /**
     * Removes all selections.
     *
     */
    public void clearSelections() {
        this.selections.clear();
    }

    /**
     * Builds a new list of ObservableRows.
     *
     * @return the list
     */
    public List<ObservableRow> buildObservableRows() {
        List<ObservableRow> list = new ArrayList<>();
        if (this.isEmpty()) {
            csv.clearQuery();
            for (int y = 0; y < csv.getRowCount(); y++) {
                list.add(new ObservableRow(csv.getRow(y), csv, main));
            }
        } else {
            buildQuery();
            for (Row r : csv.getRowsByQuery(csv.getQuery())) {
                list.add(new ObservableRow(r, csv, main));
            }
        }
        return list;
    }

    /**
     * Builds a new query.
     */
    private void buildQuery() {
//        Query.Where where = csv.findByQuery().where();
        Query q = csv.getQuery();
        for (Selection s : selections) {
            String[] arr = new String[s.getValues().size()];
            int x = 0;
            for (String v : s.getValues()) {
                arr[x] = v;
                x++;
            }
//            where = where.column(s.getColumn()).in(arr);
            q.in((StringColumn)s.getColumn(), arr);
        }
    }

    /**
     * Finds all selected values in the column.
     *
     * @param column the column
     * @return the selection
     */
    public Selection findSelectionByColumn(final Column column) {
        for (Selection s : selections) {
            if (s.getColumn().equals(column)) {
                return s;
            }
        }
        return null;
    }

    /**
     * Adds a new selection.
     *
     * @param column the column
     * @param value  the value
     */
    public void addSelection(final Column column, final String value) {
        Selection s = findSelectionByColumn(column);
        if (s == null) {
            s = new Selection(column);
            s.addValue(value);
            this.selections.add(s);
        } else {
            s.addValue(value);
        }
    }

    /**
     * Removes the selection with the column and value combination.
     *
     * @param column the column
     * @param value  the value
     */
    public void removeSelection(final Column column, final String value) {
        Selection s = findSelectionByColumn(column);
        if (s != null) {
            s.removeValue(value);
            if (s.isEmpty()) {
                this.selections.remove(s);
            }
        }
    }

    /**
     * Returns whether the selection is empty or not.
     *
     * @return true if the are no selections
     */
    public boolean isEmpty() {
        return selections.isEmpty();
    }

    /**
     * Returns true if the value is selected.
     *
     * @param column the column
     * @param value the value
     * @return true if selected
     */
    public boolean isSelected(final Column column, final String value) {
        Selection s = findSelectionByColumn(column);
        if (s != null) {
            for (String v : s.getValues()) {
                if (v.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }
}
