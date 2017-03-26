package org.laukvik.csv.query;

import org.laukvik.csv.columns.Column;

/**
 * Matches none of the specified matchesRow.
 */
public final class NotMatcher implements ValueMatcher<Object> {

    /**
     * The matchers which should fail.
     */
    private final ValueMatcher[] matchers;

    /**
     * Creates a new instance with the matchers.
     *
     * @param valueMatchers the matchers
     */
    public NotMatcher(final ValueMatcher... valueMatchers) {
        this.matchers = valueMatchers;
    }

    @Override
    public Column getColumn() {
        return null;
    }

    /**
     * Returns true when no matchesRow returns true.
     *
     * @param value the value
     * @return true when none matchesRow
     */
    public boolean matches(final Object value) {
        for (ValueMatcher m : matchers) {
            if (m.matches(value)) {
                return false;
            }
        }
        return true;
    }
}
