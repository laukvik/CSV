package org.laukvik.csv.fx;

import org.laukvik.csv.columns.StringColumn;
import org.laukvik.csv.query.RowMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Morten Laukvik
 */
public class StringColumnMatcherControl extends ColumnMatcherControl {

    private final List<RowMatcher> rowMatchers;

    /**
     *
     * @param column
     */
    public StringColumnMatcherControl(final StringColumn column) {
        super(column);
        rowMatchers = new ArrayList<>();
    }

    @Override
    public List<RowMatcher> getMatchers() {
        return null;
    }
}
