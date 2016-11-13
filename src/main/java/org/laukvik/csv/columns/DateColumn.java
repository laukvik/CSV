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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
    private DateFormat dateFormat;
    /**
     * The dateFormat as a String.
     */
    private String format;

    /**
     * Creates a new column with the columnName and dateFormat.
     *
     * @param columnName   the name of the column
     * @param dateFormatPattern the data format
     */
    public DateColumn(final String columnName, final String dateFormatPattern) {
        super(columnName);
        this.format = dateFormatPattern;
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
     * Sets the new dateformat pattern.
     * @param dateFormatPattern the pattern
     */
    public void setFormat(final String dateFormatPattern) {
        this.format = dateFormatPattern;
        this.dateFormat = new SimpleDateFormat(format);
    }

    /**
     * Compares date one and another.
     *
     * @param one     one column
     * @param another another column
     * @return the comparison
     */
    public static int compareDates(final Date one, final Date another) {
        if (one == null && another == null) {
            return 0;
        } else if (one == null && another != null) {
            return -1;
        } else if (one != null && another == null) {
            return 1;
        }
        return one.compareTo(another);
    }

    /**
     * Returns true if the first date is less than the second.
     *
     * @param d1 the first date
     * @param d2 the second date
     * @return true if the first date is lesser
     */
    public static boolean isLessThan(final Date d1, final Date d2) {
        return compareDates(d1, d2) < 1;
    }

    /**
     * Returns true if the first date is greater than the second.
     *
     * @param d1 the first date
     * @param d2 the second date
     * @return true if the first date is greater
     */
    public static boolean isGreaterThan(final Date d1, final Date d2) {
        return compareDates(d1, d2) > 0;
    }

    /**
     * Returns true if the date part is the same in both dates ignoring the time.
     *
     * @param d1 first date
     * @param d2 second date
     * @return true if the date is the same
     */
    public static boolean isEqualDate(final Date d1, final Date d2) {
        if (d1 == null && d2 == null) {
            return true;
        } else if (d1 == null) {
            return false;
        } else if (d2 == null) {
            return false;
        }
        GregorianCalendar c1 = new GregorianCalendar();
        c1.setTime(d1);
        GregorianCalendar c2 = new GregorianCalendar();
        c2.setTime(d2);
        return c1.get(Calendar.DATE) == c2.get(Calendar.DATE)
                && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
                && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);
    }

    /**
     * Returns true if the time part is the same in both dates ignoring the date.
     *
     * @param d1 first date
     * @param d2 second date
     * @return true if the time is the same
     */
    public static boolean isEqualTime(final Date d1, final Date d2) {
        if (d1 == null && d2 == null) {
            return true;
        } else if (d1 == null) {
            return false;
        } else if (d2 == null) {
            return false;
        }
        GregorianCalendar c1 = new GregorianCalendar();
        c1.setTime(d1);
        GregorianCalendar c2 = new GregorianCalendar();
        c2.setTime(d2);
        return c1.get(Calendar.HOUR_OF_DAY) == c2.get(Calendar.HOUR_OF_DAY)
                && c1.get(Calendar.MINUTE) == c2.get(Calendar.MINUTE)
                && c1.get(Calendar.SECOND) == c2.get(Calendar.SECOND);
    }

    /**
     * Returns true if the date is the year.
     *
     * @param v    the date
     * @param year the year
     * @return true if the date is the year
     */
    public static boolean isYear(final Date v, final int year) {
        if (v == null) {
            return false;
        }
        Calendar c = new GregorianCalendar();
        c.setTime(v);
        return c.get(GregorianCalendar.YEAR) == year;
    }

    /**
     * Returns true if date has the year greater than the year.
     *
     * @param v    the date
     * @param year the year
     * @return true if date has the year greater than the year.
     */
    public static boolean isYearGreaterThan(final Date v, final int year) {
        if (v == null) {
            return false;
        }
        Calendar c = new GregorianCalendar();
        c.setTime(v);
        return c.get(GregorianCalendar.YEAR) > year;
    }

    /**
     * Returns true if date has the year less than the year.
     *
     * @param v    the date
     * @param year the year
     * @return true if date has the year less than the year.
     */
    public static boolean isYearLessThan(final Date v, final int year) {
        if (v == null) {
            return false;
        }
        Calendar c = new GregorianCalendar();
        c.setTime(v);
        return c.get(GregorianCalendar.YEAR) < year;
    }

    /**
     * Returns true if the date is between the two years.
     *
     * @param v      the date
     * @param year   the first year
     * @param toYear the second year
     * @return true if the date is between the two years.
     */
    public static boolean isYearBetween(final Date v, final int year, final int toYear) {
        if (v == null) {
            return false;
        }
        Calendar c = new GregorianCalendar();
        c.setTime(v);
        int thisYear = c.get(GregorianCalendar.YEAR);
        return thisYear >= year && thisYear <= toYear;
    }

    /**
     * Returns true if the date is the same month.
     *
     * @param v     the date
     * @param month the month
     * @return returns true if the month is same
     */
    public static boolean isMonth(final Date v, final int month) {
        if (v == null) {
            return false;
        }
        Calendar c = new GregorianCalendar();
        c.setTime(v);
        return c.get(GregorianCalendar.MONTH) == month;
    }

    /**
     * Returns true if the date has the week.
     *
     * @param v    the date
     * @param week week number
     * @return true if the date has the week
     */
    public static boolean isWeek(final Date v, final int week) {
        if (v == null) {
            return false;
        }
        Calendar c = new GregorianCalendar();
        c.setTime(v);
        return c.get(GregorianCalendar.WEEK_OF_YEAR) == week;
    }

    /**
     * Returns true if the date has the dayOfWeek.
     *
     * @param v         the date value
     * @param dayOfWeek the day of week
     * @return returns true if the date has the dayOfWeek.
     */
    public static boolean isDayOfWeek(final Date v, final int dayOfWeek) {
        if (v == null) {
            return false;
        }
        Calendar c = new GregorianCalendar();
        c.setTime(v);
        return c.get(GregorianCalendar.DAY_OF_WEEK) == dayOfWeek;
    }

    /**
     * Returns the date format.
     *
     * @return the date format
     */
    public String getFormat() {
        return format;
    }

    /**
     * Returns the value as a String.
     *
     * @param value the value
     * @return the value as String
     */
    public String asString(final Date value) {
        return dateFormat.format(value);
    }

    /**
     * Returns the Date by parsing the value.
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

}
