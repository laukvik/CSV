package org.laukvik.csv.query;

/**
 * Returns true when the value the criteria the the value matcher.
 *
 * @param <T> The type of class
 */
interface ValueMatcher<T> {

    /**
     * Returns true when the values match.
     *
     * @param value the value to test against
     * @return true when matches.
     */
    boolean matches(T value);

}
