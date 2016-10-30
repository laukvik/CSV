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
    protected final Date value;
    /**
     * The DateColumn.
     */
    protected final DateColumn column;

    /**
     * Creats a new matcher.
     *
     * @param dateColumn the dateColumn
     * @param value      the date
     */
    AbstractDateMatcher(final DateColumn dateColumn, final Date value) {
        super();
        this.column = dateColumn;
        this.value = value;
    }

    /**
     * @param d1
     * @param d2
     * @return
     */
    private static int compare(final Date d1, final Date d2) {
        return d1.compareTo(d2);
    }

    public static boolean isLessThan(final Date d1, final Date d2) {
        return compare(d1, d2) < 1;
    }

    static boolean isGreaterThan(final Date d1, final Date d2) {
        return compare(d1, d2) > 0;
    }

    public static boolean isEqualDate(final Date d1, final Date d2) {
        GregorianCalendar c1 = new GregorianCalendar();
        c1.setTime(d1);
        GregorianCalendar c2 = new GregorianCalendar();
        c2.setTime(d2);
        return c1.get(Calendar.DATE) == c2.get(Calendar.DATE)
                && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
                && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);
    }

    public static boolean isEqualTime(final Date d1, final Date d2) {
        GregorianCalendar c1 = new GregorianCalendar();
        c1.setTime(d1);
        GregorianCalendar c2 = new GregorianCalendar();
        c2.setTime(d2);
        return c1.get(Calendar.HOUR_OF_DAY) == c2.get(Calendar.HOUR_OF_DAY)
                && c1.get(Calendar.MINUTE) == c2.get(Calendar.MINUTE)
                && c1.get(Calendar.SECOND) == c2.get(Calendar.SECOND);
    }

    static boolean isYear(final Date v, final int year) {
        if (v == null) {
            return false;
        }
        Calendar c = new GregorianCalendar();
        c.setTime(v);
        return c.get(GregorianCalendar.YEAR) == year;
    }

    public static boolean isYearGreaterThan(final Date v, final int year) {
        if (v == null) {
            return false;
        }
        Calendar c = new GregorianCalendar();
        c.setTime(v);
        return c.get(GregorianCalendar.YEAR) > year;
    }

    public static boolean isYearLessThan(final Date v, final int year) {
        if (v == null) {
            return false;
        }
        Calendar c = new GregorianCalendar();
        c.setTime(v);
        return c.get(GregorianCalendar.YEAR) < year;
    }

    public static boolean isYearBetween(final Date v, final int year, final int toYear) {
        if (v == null) {
            return false;
        }
        Calendar c = new GregorianCalendar();
        c.setTime(v);
        int thisYear = c.get(GregorianCalendar.YEAR);
        return thisYear >= year && thisYear <= toYear;
    }

    public static boolean isMonth(final Date v, final int month) {
        if (v == null) {
            return false;
        }
        Calendar c = new GregorianCalendar();
        c.setTime(v);
        return c.get(GregorianCalendar.MONTH) == month;
    }

    public static boolean isWeek(final Date v, final int week) {
        if (v == null) {
            return false;
        }
        Calendar c = new GregorianCalendar();
        c.setTime(v);
        return c.get(GregorianCalendar.WEEK_OF_YEAR) == week;
    }

    public static boolean isDayOfWeek(final Date v, final int dayOfWeek) {
        if (v == null) {
            return false;
        }
        Calendar c = new GregorianCalendar();
        c.setTime(v);
        return c.get(GregorianCalendar.DAY_OF_WEEK) == dayOfWeek;
    }

}
