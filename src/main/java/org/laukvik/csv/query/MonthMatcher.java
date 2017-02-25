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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Compares a DateColumn to have the year to be the specified value.
 *
 */
public final class MonthMatcher extends RowMatcher implements ValueMatcher<Date> {

    /**
     * The value to compare.
     */
    private final List<Integer> values;
    /**
     * The column to compare.
     */
    private final DateColumn column;

    /**
     * Matches the value of the dateColumn to have the specified month.
     *
     * @param dateColumn the dateColumn
     * @param value       the month
     */
    public MonthMatcher(final DateColumn dateColumn, final Integer... value) {
        this(dateColumn, Arrays.asList(value));
    }

    public MonthMatcher(final DateColumn dateColumn, final List<Integer> values) {
        super();
        this.column = dateColumn;
        this.values = values;
    }

    /**
     * Matches the row.
     *
     * @param row the row to compare
     * @return true if it matches
     */
    public boolean matches(final Row row) {
        Date v = row.getDate(column);
        return matches(v);
    }

    @Override
    public boolean matches(final Date value) {
        Integer month = DateColumn.getMonth(value);
        for (Integer v : values){
            if (month == null){
                if (v == null){
                    return true;
                }
            } else if (month.equals(v)) {
                return true;
            }
        }
        return false;
    }
}
