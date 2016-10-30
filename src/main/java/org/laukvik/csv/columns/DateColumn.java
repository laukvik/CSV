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
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Column with Date as the data type.
 */
public final class DateColumn extends Column<Date> {

    /**
     * The default date format.
     */
    public static final String DATE_FORMAT = "yyyy.MM.dd HH:mm:ss";

    /**
     * The DateFormat to use when reading and writing.
     */
    private final DateFormat dateFormat;
    /**
     * The dateFormat as a String.
     */
    private final String format;

    /**
     * Creates a new column with the columnName and dateFormat.
     *
     * @param columnName   the name of the column
     * @param dateFormat the data format
     */
    public DateColumn(final String columnName, final String dateFormat) {
        super(columnName);
        this.format = dateFormat;
        this.dateFormat = new SimpleDateFormat(format);
    }

    /**
     * Creates a new column with the columnName and default dateFormat.
     *
     * @param columnName the column name
     */
    public DateColumn(final String columnName) {
        super(columnName);
        this.format = DATE_FORMAT;
        this.dateFormat = new SimpleDateFormat(DATE_FORMAT);
    }

    /**
     *
     * @return
     */
    public String getFormat() {
        return format;
    }

    /**
     *
     * @return
     */
    public DateFormat getDateFormat() {
        return dateFormat;
    }

    /**
     * Returns the value as a String
     *
     * @param value the value
     * @return the value as String
     */
    public String asString(final Date value) {
        return dateFormat.format(value);
    }

    /**
     * Returns the Date by parsing the value
     *
     * @param value the string
     * @return the date
     */
    public Date parse(final String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return dateFormat.parse(value);
        } catch (ParseException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Compares date one and another.
     *
     * @param one     one column
     * @param another another column
     * @return the comparison
     */
    public int compare(final Date one, final Date another) {
        return one.compareTo(another);
    }

    /**
     * Returns the Column as String.
     * @return the Column as String
     */
    public String toString() {
        return getName() + "(Date)";
    }

    /**
     * Returns the HashCode.
     * @return the HashCode
     */
    public int hashCode() {
        return 3;
    }

    /**
     * Returns true if obj is the same.
     *
     * @param obj object to compare with
     * @return
     */
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DateColumn other = (DateColumn) obj;
        return true;
    }

}
