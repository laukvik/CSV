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

import org.laukvik.csv.Row;
import org.laukvik.csv.columns.DateColumn;

import java.util.Date;

/**
 * Compares a DateColumn to have the year to be the specified value.
 *
 */
public final class YearIs extends AbstractDateMatcher {

    /**
     * The year.
     */
    private final int year;

    /**
     * Matches the value of the dateColumn to have the specified year.
     *
     * @param dateColumn the dateColumn
     * @param year       the year
     */
    public YearIs(final DateColumn dateColumn, final int year) {
        super(dateColumn, null);
        this.year = year;
    }

    /**
     * Matches the row.
     *
     * @param row the row to compare
     * @return true if it matches
     */
    public boolean matches(final Row row) {
        Date v = row.getDate(column);
        return DateColumn.isYear(v, year);
    }

}
