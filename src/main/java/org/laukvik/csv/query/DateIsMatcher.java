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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Compares a DateColumn to specified Date
 */
public class DateIsMatcher extends RowMatcher {

    private final Date value;
    private final DateColumn column;

    public DateIsMatcher(DateColumn column, Date value) {
        super();
        this.column = column;
        this.value = value;
    }

    @Override
    public boolean matches(Row row) {
        Date d = row.getDate(column);
        if (d == null) {
            return false;
        }
        GregorianCalendar c1 = new GregorianCalendar();
        c1.setTime(value);
        GregorianCalendar c2 = new GregorianCalendar();
        c2.setTime(d);
        return c1.get(Calendar.DATE) == c2.get(Calendar.DATE) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);
    }

}
