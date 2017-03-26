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

import java.util.Date;

/**
 * Compares a DateColumn to an array of Dates.
 *
 */
public final class DateBetweenMatcher extends AbstractDateMatcher {

    /**
     * The earliest date.
     */
    private final Date firstDate;

    /**
     * The latest date.
     */
    private final Date lastDate;

    /**
     * Compares a DateColumn between two dates for the column.
     *
     * @param column the column
     * @param first the first date
     * @param last the last date
     */
    public DateBetweenMatcher(final DateColumn column, final Date first, final Date last) {
        super(column, null);
        this.firstDate = first;
        this.lastDate = last;
    }

    @Override
    public boolean matches(final Date value) {
        return DateColumn.isBetweeen(value, firstDate, lastDate);
    }
}
