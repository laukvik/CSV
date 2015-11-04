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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.DateColumn;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class YearIs extends RowMatcher {

    int value;
    DateFormat format;
    DateColumn column;

    public YearIs(DateColumn column, int value, DateFormat format) {
        super();
        this.value = value;
        this.format = format;
        this.column = column;
    }

    @Override
    public boolean mathes(Row row) {
        Date v = row.getDate(column);
        if (v == null) {
            return false;
        }
        Calendar c = new GregorianCalendar();
        c.setTime(v);
        return c.get(GregorianCalendar.YEAR) == value;
    }

}
