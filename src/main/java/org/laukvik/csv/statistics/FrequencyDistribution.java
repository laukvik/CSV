/*
 * Copyright 2015 Laukviks Bedrifter.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laukvik.csv.statistics;

import org.laukvik.csv.columns.Column;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


/**
 * Frequency distribution is a table that shows the frequency of values found in a
 * column. Each entry contains the value and the count of occurrences of that value.
 *
 * @param <T> the type
 */
public final class FrequencyDistribution<T> {

    /**
     * The column.
     */
    private final Column column;
    /**
     * The map with string value and the frequency.
     */
    private final Map<T, Integer> map;

    /**
     * The amount of nulls.
     */
    private int nulls;

    /**
     * Creates a new FrequencyDistribution with empty values for the
     * specified column.
     *
     * @param column the column
     */
    public FrequencyDistribution(final Column column) {
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
     * Returns the set of keys.
     *
     * @return the set of keys
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
    public Integer getCount(final String key) {
        return map.get(key);
    }

    /**
     * Adds a value. Nulls are treated as empty strings.
     *
     * @param value the value to add
     */
    public void addValue(final T value) {
        if (value == null) {
            this.nulls++;
        } else if (map.containsKey(value)) {
            Integer count = map.get(value) + 1;
            map.put(value, count);
        } else {
            map.put(value, 1);
        }
    }

    /**
     * Returns the amount of nulls found.
     *
     * @return the amount
     */
    public int getNullCount() {
        return nulls;
    }
}
