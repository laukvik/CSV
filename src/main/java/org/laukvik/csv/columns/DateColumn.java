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
package org.laukvik.csv.columns;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class DateColumn implements Column<Date> {

    String name;
    DateFormat format;

    public DateColumn(String name, DateFormat format) {
        this.name = name;
        this.format = format;
    }

    public DateColumn(DateFormat format) {
        this.format = format;
    }

    public DateFormat getFormat() {
        return format;
    }

    public boolean isYear(Date d, int year) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(d);
        return c.get(Calendar.YEAR) == year;
    }

    public boolean isMonth(Date d, int month) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(d);
        return c.get(Calendar.MONTH) == month;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String asString(Date value) {
        return format.format(value);
    }

    @Override
    public Date parse(String value) {
        try {
            return format.parse(value);
        } catch (ParseException ex) {
            return null;
        }
    }

    public int compare(Date one, Date another) {
        return one.compareTo(another);
    }

    @Override
    public String toString() {
        return name + "(Date)";
    }


}
