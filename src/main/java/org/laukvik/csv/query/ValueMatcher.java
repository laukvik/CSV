package org.laukvik.csv.query;

/**
 * Returns true when the value the criteria the the value matcher.
 *
 */
interface ValueMatcher<T> {

    /**
     * Returns true when the values match.
     *
     * @param value the value to test against
     * @return
     */
    boolean matches(T value);

}
