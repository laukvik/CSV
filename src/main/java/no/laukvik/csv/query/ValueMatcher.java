package no.laukvik.csv.query;

import no.laukvik.csv.columns.Column;

/**
 * Returns true when the value the criteria the the value matcher.
 *
 * @param <T> The type of class
 */
public interface ValueMatcher<T> {

    /**
     * Returns true when the values match.
     *
     * @param value the value to test against
     * @return true when matchesRow.
     */
    boolean matches(T value);

    /**
     * Returns the Column for the matcher.
     *
     * @return the Column
     */
    Column getColumn();
}
