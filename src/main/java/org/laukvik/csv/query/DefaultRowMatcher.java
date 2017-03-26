package org.laukvik.csv.query;

import org.laukvik.csv.Row;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class DefaultRowMatcher implements RowMatcher {

    private final List<ValueMatcher> matchers;

    public DefaultRowMatcher(){
        matchers = new ArrayList<>();
    }

    @Override
    public boolean matchesRow(final Row row) {
        return false;
    }
}
