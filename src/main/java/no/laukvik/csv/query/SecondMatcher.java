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
import no.laukvik.csv.columns.DateColumn;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Compares a DateColumn to have the specified second.
 */
public final class SecondMatcher implements ValueMatcher<Date> {

    /**
     * The value to compare.
     */
    private final List<Integer> values;
    /**
     * The column to compare.
     */
    private final DateColumn column;

    /**
     * Compares a DateColumn to have the specified second.
     *
     * @param dateColumn the column
     * @param value      the second
     */
    public SecondMatcher(final DateColumn dateColumn, final Integer... value) {
        this(dateColumn, Arrays.asList(value));
    }

    /**
     * Compares a DateColumn to have the specified second.
     *
     * @param dateColumn the column
     * @param value      the second
     */
    public SecondMatcher(final DateColumn dateColumn, final List<Integer> value) {
        this.column = dateColumn;
        this.values = value;
    }

    @Override
    public Column getColumn() {
        return column;
    }

    @Override
    public boolean matches(final Date value) {
        return values.contains(DateColumn.getSeconds(value));
    }

}

