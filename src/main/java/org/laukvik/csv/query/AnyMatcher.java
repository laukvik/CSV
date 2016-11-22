package org.laukvik.csv.query;

import org.laukvik.csv.Row;

/**
 * Matches any of the matchers.
 *
 */
public final class AnyMatcher extends RowMatcher{

    /**
     * The list of matchers.
     */
    private RowMatcher[] matchers;

    /**
     * Creates a new instance with the specified matchers.
     *
     * @param rowMatchers one or more matchers
     */
    public AnyMatcher(final RowMatcher... rowMatchers) {
        this.matchers = rowMatchers;
    }

    /**
     * Returns true when any of the matcher returns true.
     *
     * @param row the row
     * @return true when matches
     */
    public boolean matches(final Row row) {
        for (RowMatcher m : matchers) {
            if (m.matches(row)) {
                return true;
            }
        }
        return false;
    }
}
