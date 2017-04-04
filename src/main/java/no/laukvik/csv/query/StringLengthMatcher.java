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
package no.laukvik.csv.query;

import no.laukvik.csv.columns.Column;
import no.laukvik.csv.columns.StringColumn;

import java.util.Arrays;
import java.util.List;

/**
 * Compares a StringColumn to have the specified value.
 */
public final class StringLengthMatcher implements ValueMatcher<String> {

    /**
     * The value to compare.
     */
    private final List<Integer> values;
    /**
     * The column to compare.
     */
    private final StringColumn column;

    /**
     * Matches all strings with the specified length values.
     *
     * @param stringColumn the column
     * @param value        the value
     */
    public StringLengthMatcher(final StringColumn stringColumn, final Integer... value) {
        this(stringColumn, Arrays.asList(value));
    }

    /**
     * Matches all strings with the specified length values.
     *
     * @param stringColumn the column
     * @param values       the value
     */
    public StringLengthMatcher(final StringColumn stringColumn, final List<Integer> values) {
        super();
        this.column = stringColumn;
        this.values = values;
    }

    @Override
    public Column getColumn() {
        return column;
    }

    @Override
    public boolean matches(final String value) {
        Integer l = StringColumn.getLength(value);
        return values.contains(l);
    }
}
