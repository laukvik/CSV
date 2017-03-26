package org.laukvik.csv.query;

import org.laukvik.csv.Row;

/**
 * Matches any of the matchers.
 *
 */
public final class AnyMatcher implements RowMatcher {

    /**
     * The list of matchers.
     */
    private final ValueMatcher[] matchers;

    /**
     * Creates a new instance with the specified matchers.
     *
     * @param rowMatchers one or more matchers
     */
    public AnyMatcher(final ValueMatcher... rowMatchers) {
        this.matchers = rowMatchers;
    }

    /**
     * Returns true when any of the matcher returns true.
     *
     * @param row the row
     * @return true when matchesRow
     */
    public boolean matchesRow(final Row row) {
        for (ValueMatcher m : matchers) {
            if (m.matches(row)) {
                return true;
            }
        }
        return false;
    }
}
