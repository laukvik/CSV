package org.laukvik.csv.query;

import org.laukvik.csv.columns.DateColumn;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * An abstract class that has all utility methods to be helpful.
 *
 */
public abstract class AbstractDateMatcher extends RowMatcher {

    /**
     * The date.
     */
    protected Date value;
    /**
     * The DateColumn.
     */
    protected DateColumn column;

    /**
     * Creats a new matcher.
     *
     * @param dateColumn the dateColumn
     * @param date      the date
     */
    AbstractDateMatcher(final DateColumn dateColumn, final Date date) {
        super();
        this.column = dateColumn;
        this.value = date;
    }

    /**
     * Compares the two dates.
     *
     * @param d1 first date
     * @param d2 second date
     * @return 0 if equals, 1 if d2 is greater and -1 if less than
     */
    private static int compare(final Date d1, final Date d2) {
        return d1.compareTo(d2);
    }

    /**
     * Returns true if the first date is less than the second.
     *
     * @param d1 the first date
     * @param d2 the second date
     * @return true if the first date is lesser
     */
    public static boolean isLessThan(final Date d1, final Date d2) {
        return compare(d1, d2) < 1;
    }

    /**
     * Returns true if the first date is greater than the second.
     *
     * @param d1 the first date
     * @param d2 the second date
     * @return true if the first date is greater
     */
    public static final boolean isGreaterThan(final Date d1, final Date d2) {
        return compare(d1, d2) > 0;
    }

    /**
     * Returns true if the date part is the same in both dates ignoring the time.
     *
     * @param d1 first date
     * @param d2 second date
     * @return true if the date is the same
     */
    public static boolean isEqualDate(final Date d1, final Date d2) {
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
    public static final boolean isEqualTime(final Date d1, final Date d2) {
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
    public static final boolean isYear(final Date v, final int year) {
        if (v == null) {
            return false;
        }
        Calendar c = new GregorianCalendar();
        c.setTime(v);
        return c.get(GregorianCalendar.YEAR) == year;
    }

    /**
     * Returns true if date has the year greater than the year.
     * @param v the date
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
     * @param v the date
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
     * @param v the date
     * @param year the first year
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
    public static final boolean isMonth(final Date v, final int month) {
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
    public static final boolean isWeek(final Date v, final int week) {
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
    public static final boolean isDayOfWeek(final Date v, final int dayOfWeek) {
        if (v == null) {
            return false;
        }
        Calendar c = new GregorianCalendar();
        c.setTime(v);
        return c.get(GregorianCalendar.DAY_OF_WEEK) == dayOfWeek;
    }

}
