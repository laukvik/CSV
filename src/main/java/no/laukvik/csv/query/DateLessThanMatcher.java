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

import no.laukvik.csv.columns.DateColumn;

import java.util.Date;

/**
 * Compares a DateColumn to be less than a date.
 */
public final class DateLessThanMatcher extends AbstractDateMatcher {


    /**
     * Compares a DateColumn to be less than the value.
     *
     * @param column the dateColumn
     * @param value  the date
     */
    public DateLessThanMatcher(final DateColumn column, final Date value) {
        super(column, value);
    }

    @Override
    public boolean matches(final Date value) {
        return DateColumn.isLessThan(value, getValue());
    }
}
