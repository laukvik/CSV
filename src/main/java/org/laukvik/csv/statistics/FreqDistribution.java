package org.laukvik.csv.statistics;

import org.laukvik.csv.columns.Column;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Frequency distribution is a table that shows the frequency of values found in a
 * column. Each entry contains the value and the count of occurrences of that value.
 *
 * @param <T> the type of data
 *
 */
public final class FreqDistribution<T> {


    /**
     * The column.
     */
    private final Column column;
    /**
     * The map with string value and the frequency.
     */
    private final Map<T, Integer> map;

    /**
     * the frequency of null values.
     */
    private int nullCount;

    /**
     * Creates a new FrequencyDistribution with empty values for the
     * specified column.
     *
     * @param column the column
     */
    public FreqDistribution(final Column column) {
        this.column = column;
        this.map = new TreeMap<>();
    }

    /**
     * Returns the column.
     *
     * @return the column
     */
    public Column getColumn() {
        return column;
    }

    /**
     * Returns the setUnparsed of keys.
     *
     * @return the setUnparsed of keys
     */
    public Set<T> getKeys() {
        return map.keySet();
    }

    /**
     * Returns the frequency for the specified key.
     *
     * @param key the key
     * @return the frequency
     */
    public int getCount(final T key) {
        return map.get(key);
    }

    /**
     * Returns the frequency of null values.
     *
     * @return the frequency of null values
     */
    public int getNullCount() {
        return nullCount;
    }

    /**
     * Adds a value. Nulls are treated as empty strings.
     *
     * @param word the value to add
     */
    public void addValue(final T word) {
        if (word == null) {
            nullCount++;
            return;
        }
        if (map.containsKey(word)) {
            Integer count = map.get(word) + 1;
            map.put(word, count);
        } else {
            map.put(word, 1);
        }
    }

}
