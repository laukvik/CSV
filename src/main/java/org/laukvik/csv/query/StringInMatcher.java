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
package org.laukvik.csv.query;

import org.laukvik.csv.columns.Column;
import org.laukvik.csv.columns.StringColumn;

import java.util.Arrays;
import java.util.List;

/**
 * Compares a StringColumn to have one or more of the specified values.
 */
public final class StringInMatcher implements ValueMatcher<String> {

    /**
     * The values to match.
     */
    private final List<String> values;
    /** The Column to match. */
    private final StringColumn column;

    /**
     * The value of the column must be among the values.
     *
     * @param stringColumn the column
     * @param values       the values
     */
    public StringInMatcher(final StringColumn stringColumn, final String... values) {
        this(stringColumn, Arrays.asList(values));
    }

    /**
     * The value of the column must be among the values.
     *
     * @param stringColumn the column
     * @param values       the values
     */
    public StringInMatcher(final StringColumn stringColumn, final List<String> values) {
        this.column = stringColumn;
        this.values = values;
    }

    @Override
    public Column getColumn() {
        return column;
    }

    /**
     * Returns true if the value is among the values.
     *
     * @param value  the value
     * @param values the values
     * @return true if its among the values
     */
    public static boolean isAny(final String value, final String... values) {
        for (String v : values) {
            if (isAny(value, v)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if both strings are equal.
     *
     * @param v1 the value
     * @param v2 the other value
     * @return true if its equal
     */
    public static boolean isAny(final String v1, final String v2) {
        return v1.equals(v2);
    }


    @Override
    public boolean matches(final String value) {
        return values.contains(value);
    }
}
