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

import no.laukvik.csv.Row;
import no.laukvik.csv.columns.DateColumn;

import java.util.Date;

/**
 * Compares a DateColumn to the specified dates.
 */
public final class DateGreaterThanMatcher extends AbstractDateMatcher {

    /**
     * Compares a DateColumn to specified Date.
     *
     * @param column the dataColumn
     * @param value  the value to compare
     */
    public DateGreaterThanMatcher(final DateColumn column, final Date value) {
        super(column, value);
    }

    /**
     * Returns true when the row matchesRow.
     *
     * @param row the row
     * @return true when the row matchesRow
     */
    public boolean matches(final Row row) {
        Date d = row.get(getColumn());
        return DateColumn.isGreaterThan(d, getValue());
    }

    @Override
    public boolean matches(final Date value) {
        return DateColumn.isGreaterThan(value, getValue());
    }
}
