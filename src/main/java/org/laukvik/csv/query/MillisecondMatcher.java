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
import org.laukvik.csv.columns.DateColumn;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Compares a StringColumn to have the specified value.
 */
public final class MillisecondMatcher implements ValueMatcher<Date> {

    /**
     * The value to compare.
     */
    private final List<Integer> values;
    /**
     * The column to compare.
     */
    private final DateColumn column;

    /**
     * Compares the date against one or more values.
     *
     * @param dateColumn the column
     * @param value      the value
     */
    public MillisecondMatcher(final DateColumn dateColumn, final Integer... value) {
        this(dateColumn, Arrays.asList(value));
    }

    /**
     * Compares the date against one or more values.
     *
     * @param dateColumn the column
     * @param values      the values
     */
    public MillisecondMatcher(final DateColumn dateColumn, final List<Integer> values) {
        super();
        this.column = dateColumn;
        this.values = values;
    }

    @Override
    public Column getColumn() {
        return column;
    }

    /**
     * Returns true when the value matchesRow.
     *
     * @param value the value
     * @return true when the row matchesRow
     */
    @Override
    public boolean matches(final Date value) {
        return values.contains(DateColumn.getMilliseconds(value));
    }

}

