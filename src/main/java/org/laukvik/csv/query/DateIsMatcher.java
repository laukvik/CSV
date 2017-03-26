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

import org.laukvik.csv.columns.DateColumn;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Compares a DateColumn to specified Date.
 */
public final class DateIsMatcher extends AbstractDateMatcher implements ValueMatcher<Date> {

    /**
     * The value to compare.
     */
    private final List<Date> values;

    /**
     * Compares a DateColumn to specified Date.
     *
     * @param column the dateColumn
     * @param value  the date
     */
    public DateIsMatcher(final DateColumn column, final Date... value) {
        this(column, Arrays.asList(value));
    }

    /**
     * Compares a DateColumn to multiple dates.
     *
     * @param column the dateColumn
     * @param values the dates
     */
    public DateIsMatcher(final DateColumn column, final List<Date> values) {
        super(column, null);
        this.values = values;
    }

    @Override
    public boolean matches(final Date v) {
        return values.contains(v);
    }
}
