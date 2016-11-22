package org.laukvik.csv.query;

import org.laukvik.csv.Row;

/**
 * Matches none of the specified matches.
 *
 */
public final class NotMatcher extends RowMatcher{

    /**
     * The matchers which should fail.
     */
    private RowMatcher[] matchers;

    /**
     * Creates a new instance with the matchers.
     *
     * @param rowMatchers the matchers
     */
    public NotMatcher(final RowMatcher... rowMatchers) {
        this.matchers = rowMatchers;
    }

    /**
     * Returns true when no matches returns true.
     *
     * @param row the row
     * @return true when none matches
     */
    public boolean matches(final Row row) {
        for (RowMatcher m : matchers) {
            if (m.matches(row)) {
                return false;
            }
        }
        return true;
    }
}
