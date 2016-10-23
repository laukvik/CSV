package org.laukvik.csv.query;

import org.laukvik.csv.columns.DateColumn;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Morten Laukvik
 */
public abstract class AbstractDateMatcher extends RowMatcher {

    protected final Date value;
    protected final DateColumn column;

    public AbstractDateMatcher(DateColumn column, Date value) {
        super();
        this.column = column;
        this.value = value;
    }

    public static int compare(Date d1, Date d2) {
        return d1.compareTo(d2);
    }

    public static boolean isLessThan(Date d1, Date d2) {
        return compare(d1, d2) < 1;
    }

    public static boolean isGreaterThan(Date d1, Date d2) {
        return compare(d1, d2) > 0;
    }

    public static boolean isEqualDate(Date d1, Date d2) {
        GregorianCalendar c1 = new GregorianCalendar();
        c1.setTime(d1);
        GregorianCalendar c2 = new GregorianCalendar();
        c2.setTime(d2);
        return c1.get(Calendar.DATE) == c2.get(Calendar.DATE) &&
                c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) &&
                c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);
    }

    public static boolean isEqualTime(Date d1, Date d2) {
        GregorianCalendar c1 = new GregorianCalendar();
        c1.setTime(d1);
        GregorianCalendar c2 = new GregorianCalendar();
        c2.setTime(d2);
        return c1.get(Calendar.HOUR_OF_DAY) == c2.get(Calendar.HOUR_OF_DAY) &&
                c1.get(Calendar.MINUTE) == c2.get(Calendar.MINUTE)
                && c1.get(Calendar.SECOND) == c2.get(Calendar.SECOND);
    }

    public static boolean isYear(Date v, int year) {
        if (v == null) {
            return false;
        }
        Calendar c = new GregorianCalendar();
        c.setTime(v);
        return c.get(GregorianCalendar.YEAR) == year;
    }

    public static boolean isYearGreaterThan(Date v, int year) {
        if (v == null) {
            return false;
        }
        Calendar c = new GregorianCalendar();
        c.setTime(v);
        return c.get(GregorianCalendar.YEAR) > year;
    }

    public static boolean isYearLessThan(Date v, int year) {
        if (v == null) {
            return false;
        }
        Calendar c = new GregorianCalendar();
        c.setTime(v);
        return c.get(GregorianCalendar.YEAR) < year;
    }

    public static boolean isYearBetween(Date v, int year, int toYear) {
        if (v == null) {
            return false;
        }
        Calendar c = new GregorianCalendar();
        c.setTime(v);
        int thisYear = c.get(GregorianCalendar.YEAR);
        return thisYear >= year && thisYear <= toYear;
    }

    public static boolean isMonth(Date v, int month) {
        if (v == null) {
            return false;
        }
        Calendar c = new GregorianCalendar();
        c.setTime(v);
        return c.get(GregorianCalendar.MONTH) == month;
    }

    public static boolean isWeek(Date v, int week) {
        if (v == null) {
            return false;
        }
        Calendar c = new GregorianCalendar();
        c.setTime(v);
        return c.get(GregorianCalendar.WEEK_OF_YEAR) == week;
    }

    public static boolean isDayOfWeek(Date v, int dayOfWeek) {
        if (v == null) {
            return false;
        }
        Calendar c = new GregorianCalendar();
        c.setTime(v);
        return c.get(GregorianCalendar.DAY_OF_WEEK) == dayOfWeek;
    }

}
