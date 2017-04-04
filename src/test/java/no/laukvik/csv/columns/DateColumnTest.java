package no.laukvik.csv.columns;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 */
public class DateColumnTest {

    private static Date getDate(int year, int month, int day, int hour, int min, int sec, int ms) {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, sec);
        cal.set(Calendar.MILLISECOND, ms);
        return cal.getTime();
    }

    private Date getTomorrow() {
        Calendar cal = new GregorianCalendar();
        Date today = cal.getTime();
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
    }

    private Date getMonthPlus() {
        Calendar cal = new GregorianCalendar();
        Date today = cal.getTime();
        cal.add(Calendar.DATE, 1);
        cal.add(Calendar.MONTH, 1);
        return cal.getTime();
    }

    private Date getYearPlus() {
        Calendar cal = new GregorianCalendar();
        Date today = cal.getTime();
        cal.add(Calendar.DATE, 1);
        cal.add(Calendar.YEAR, 1);
        return cal.getTime();
    }

    private Date getDatePlus() {
        Calendar cal = new GregorianCalendar();
        Date today = cal.getTime();
        cal.add(Calendar.DATE, 1);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    private Date getHourPlus() {
        Calendar cal = new GregorianCalendar();
        Date today = cal.getTime();
        cal.add(Calendar.DATE, 1);
        cal.add(Calendar.HOUR_OF_DAY, 1);
        return cal.getTime();
    }

    private Date getMinutePlus() {
        Calendar cal = new GregorianCalendar();
        Date today = cal.getTime();
        cal.add(Calendar.DATE, 1);
        cal.add(Calendar.MINUTE, 1);
        return cal.getTime();
    }

    private Date getSecondsPlus() {
        Calendar cal = new GregorianCalendar();
        Date today = cal.getTime();
        cal.add(Calendar.DATE, 1);
        cal.add(Calendar.SECOND, 1);
        return cal.getTime();
    }

    @Test
    public void constructor() throws Exception {
        DateColumn c = new DateColumn("created");
        assertEquals("created", c.getName());
        assertEquals(DateColumn.DEFAULT_FORMAT, c.getFormat());
    }

    @Test
    public void compare() throws Exception {
        DateColumn c = new DateColumn("created");
        Calendar cal = new GregorianCalendar();
        Date today = cal.getTime();
        cal.add(Calendar.DATE, 1);
        cal.add(Calendar.MINUTE, 1);
        Date tomorrow = getTomorrow();
        assertEquals(-1, c.compare(today, tomorrow));
        assertEquals(1, c.compare(tomorrow, today));
        assertEquals(1, c.compare(tomorrow, null));
        assertEquals(-1, c.compare(null, cal.getTime()));
        assertEquals(0, c.compare(null, null));
    }

    @Test
    public void isLessThan() throws Exception {
        Date tomorrow = getTomorrow();
        assertTrue(DateColumn.isGreaterThan(tomorrow, new Date()));
        assertFalse(DateColumn.isGreaterThan(null, new Date()));
        assertTrue(DateColumn.isGreaterThan(new Date(), null));
    }

    @Test
    public void isGreaterThan() throws Exception {
        Date tomorrow = getTomorrow();
        assertTrue(DateColumn.isGreaterThan(tomorrow, new Date()));
        assertFalse(DateColumn.isGreaterThan(new Date(), tomorrow));
        assertFalse(DateColumn.isGreaterThan(tomorrow, tomorrow));
        assertTrue(DateColumn.isGreaterThan(tomorrow, null));
        assertFalse(DateColumn.isGreaterThan(null, tomorrow));
    }

    @Test
    public void isEqualDate() throws Exception {
        Date tomorrow = getTomorrow();
        assertTrue(DateColumn.isEqualDate(tomorrow, tomorrow));
        assertFalse(DateColumn.isEqualDate(tomorrow, null));
        assertFalse(DateColumn.isEqualDate(null, tomorrow));
        assertTrue(DateColumn.isEqualDate(null, null));

        assertTrue(DateColumn.isEqualDate(tomorrow, getHourPlus()));
        assertFalse(DateColumn.isEqualDate(tomorrow, getDatePlus()));
        assertTrue(DateColumn.isEqualDate(tomorrow, getMinutePlus()));
        assertFalse(DateColumn.isEqualDate(tomorrow, getYearPlus()));
        assertFalse(DateColumn.isEqualDate(tomorrow, getMonthPlus()));
    }

    @Test
    public void isEqualTime() throws Exception {
        Date tomorrow = getTomorrow();
        assertTrue(DateColumn.isEqualTime(tomorrow, tomorrow));

        assertFalse(DateColumn.isEqualTime(tomorrow, getHourPlus()));
        assertFalse(DateColumn.isEqualTime(tomorrow, getMinutePlus()));
        assertFalse(DateColumn.isEqualTime(tomorrow, getSecondsPlus()));

        assertTrue(DateColumn.isEqualTime(tomorrow, getDatePlus()));
        assertTrue(DateColumn.isEqualTime(tomorrow, getMonthPlus()));
        assertTrue(DateColumn.isEqualTime(tomorrow, getYearPlus()));

        assertFalse(DateColumn.isEqualTime(tomorrow, null));
        assertFalse(DateColumn.isEqualTime(null, tomorrow));
        assertTrue(DateColumn.isEqualTime(null, null));
    }

    @Test
    public void isYear() throws Exception {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, 2015);
        Date today = cal.getTime();
        assertTrue(DateColumn.isYear(today, 2015));
        assertTrue(DateColumn.isYear(today, cal.get(Calendar.YEAR)));
        assertFalse(DateColumn.isYear(null, cal.get(Calendar.YEAR)));
    }

    @Test
    public void isYearGreaterThan() throws Exception {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.YEAR, 1);
        assertFalse(DateColumn.isYearGreaterThan(new Date(), cal.get(Calendar.YEAR)));
        assertFalse(DateColumn.isYearGreaterThan(null, cal.get(Calendar.YEAR)));
        assertTrue(DateColumn.isYearGreaterThan(new Date(), cal.get(Calendar.YEAR) - 3));
        assertFalse(DateColumn.isYearGreaterThan(new Date(), cal.get(Calendar.YEAR)));
    }

    @Test
    public void isYearLessThan() throws Exception {
        Calendar cal = new GregorianCalendar();

        assertFalse(DateColumn.isYearLessThan(cal.getTime(), cal.get(Calendar.YEAR)));

        cal.add(Calendar.YEAR, -1);
        assertFalse(DateColumn.isYearLessThan(new Date(), cal.get(Calendar.YEAR)));

        cal.add(Calendar.YEAR, 5);
        assertTrue(DateColumn.isYearLessThan(new Date(), cal.get(Calendar.YEAR)));

        assertFalse(DateColumn.isYearLessThan(null, cal.get(Calendar.YEAR)));
    }

    @Test
    public void isDateOfMonth() {
        Calendar cal = new GregorianCalendar();
        Date now = cal.getTime();
        Calendar cal2 = new GregorianCalendar();
        cal2.add(Calendar.DAY_OF_MONTH, 2);

        assertTrue(DateColumn.isDateOfMonth(now, cal.get(Calendar.DAY_OF_MONTH)));
        assertFalse(DateColumn.isDateOfMonth(now, cal2.get(Calendar.DAY_OF_MONTH)));
        assertFalse(DateColumn.isDateOfMonth(null, cal2.get(Calendar.DAY_OF_MONTH)));

    }

    @Test
    public void isYearBetween() throws Exception {
        Calendar cal = new GregorianCalendar();
        int thisYear = cal.get(Calendar.YEAR);
        cal.add(Calendar.YEAR, -1);
        int lastYear = cal.get(Calendar.YEAR);
        cal.add(Calendar.YEAR, 2);
        int nextYear = cal.get(Calendar.YEAR);
        cal.add(Calendar.YEAR, 8);
        Date tenYearDate = cal.getTime();
        assertTrue(DateColumn.isYearBetween(new Date(), lastYear, nextYear));
        assertFalse(DateColumn.isYearBetween(new Date(), nextYear, lastYear));
        assertTrue(DateColumn.isYearBetween(new Date(), thisYear, thisYear));
        assertFalse(DateColumn.isYearBetween(new Date(), thisYear, lastYear));
        assertTrue(DateColumn.isYearBetween(new Date(), lastYear, thisYear));
        assertFalse(DateColumn.isYearBetween(null, thisYear, lastYear));

        assertFalse(DateColumn.isYearBetween(tenYearDate, lastYear, lastYear));
    }

    @Test
    public void isMonth() throws Exception {
        Calendar cal = new GregorianCalendar();
        int month = cal.get(Calendar.MONTH);
        assertTrue(DateColumn.isMonth(new Date(), month));
        assertFalse(DateColumn.isMonth(new Date(), month + 1));
        assertFalse(DateColumn.isMonth(new Date(), month - 1));
    }

    @Test
    public void isWeek() throws Exception {
        Calendar cal = new GregorianCalendar();
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        assertTrue(DateColumn.isWeek(new Date(), week));
        assertFalse(DateColumn.isWeek(new Date(), week + 1));
        assertFalse(DateColumn.isWeek(new Date(), week - 1));
    }

    @Test
    public void isDayOfWeek() throws Exception {
        Calendar cal = new GregorianCalendar();
        int dow = cal.get(Calendar.DAY_OF_WEEK);
        assertTrue(DateColumn.isDayOfWeek(new Date(), dow));
        assertFalse(DateColumn.isDayOfWeek(new Date(), dow + 1));
        assertFalse(DateColumn.isDayOfWeek(new Date(), dow - 1));

        assertFalse(DateColumn.isDayOfWeek(null, dow));
    }

    @Test
    public void getFormat() throws Exception {
        DateColumn dc = new DateColumn("created", "dd.MM.YYYY hh.mm.ss");
        assertEquals("dd.MM.YYYY hh.mm.ss", dc.getFormat());
    }

    @Test
    public void asString() throws Exception {
        DateColumn dc = new DateColumn("created", "dd.MM.YYYY HH.mm.ss");
        Calendar cal = new GregorianCalendar();
        cal.set(2000, 11, 24, 18, 30, 0);
        assertEquals("24.12.2000 18.30.00", dc.asString(cal.getTime()));
    }

    @Test
    public void parse() throws Exception {
        DateColumn c = new DateColumn("created", "yyyy-MM-dd HH:mm:ss");
        Calendar cal = new GregorianCalendar();
        String dateString = "2000-01-01 00:00:00";
        Date d = c.parse(dateString);
        assertEquals(dateString, c.asString(d));
        assertNull(c.parse(""));
        assertNull(c.parse(" "));
        assertNull(c.parse("LXLASDLASD"));
        assertNull(c.parse(null));
    }

    @Test
    public void setFormat() throws Exception {
        DateColumn dc = new DateColumn("created");
        dc.setFormat("dd.MM.YYYY HH.mm.ss");
        assertEquals("dd.MM.YYYY HH.mm.ss", dc.getFormat());
    }

    @Test
    public void compareDates() throws Exception {
        DateColumn c = new DateColumn("created");
        Calendar cal = new GregorianCalendar();
        Date today = cal.getTime();
        cal.add(Calendar.DATE, 1);
        Date tomorrow = cal.getTime();
        assertEquals(-1, DateColumn.compareDates(today, tomorrow));
        assertEquals(1, DateColumn.compareDates(tomorrow, today));
        assertEquals(0, DateColumn.compareDates(tomorrow, tomorrow));
        assertEquals(1, DateColumn.compareDates(tomorrow, null));
        assertEquals(-1, DateColumn.compareDates(null, cal.getTime()));
        assertEquals(0, DateColumn.compareDates(null, null));
    }

    @Test
    public void between() throws Exception {
        DateColumn dc = new DateColumn("created", "dd.MM.YYYY");
        Date value = dc.parse("01.01.2002");
        Date d1 = dc.parse("01.01.2001");
        Date d2 = dc.parse("01.01.2002");
        Date d3 = dc.parse("01.01.2003");


        assertTrue(DateColumn.isBetweeen(value, d1, d2));
        assertTrue(DateColumn.isBetweeen(value, d2, d2));
        assertTrue(DateColumn.isBetweeen(value, d2, d3));
        assertFalse(DateColumn.isBetweeen(value, d1, null));
        assertFalse(DateColumn.isBetweeen(value, null, d2));
        assertFalse(DateColumn.isBetweeen(value, null, null));
        assertFalse(DateColumn.isBetweeen(null, null, null));

        assertFalse(DateColumn.isBetweeen(d1, value, d2));
        assertFalse(DateColumn.isBetweeen(d3, value, d2));
    }


    @Test
    public void getDayOfWeek() throws Exception {
        DateColumn dc = new DateColumn("created", "dd.MM.YYYY HH.mm.ss");
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, 2017);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        assertEquals((Integer) Calendar.SUNDAY, DateColumn.getDayOfWeek(cal.getTime()));
        assertNull(DateColumn.getDayOfWeek(null));
    }

    @Test
    public void getWeekOfYear() throws Exception {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.WEEK_OF_YEAR, 10);
        assertEquals((Integer) 10, DateColumn.getWeekOfYear(cal.getTime()));
        assertNull(DateColumn.getWeekOfYear(null));
    }


    @Test
    public void getParts() throws Exception {
        Date cal = getDate(2017, 2, 25, 16, 15, 12, 123);
        assertEquals((Integer) 2017, DateColumn.getYear(cal));
        assertEquals((Integer) 2, DateColumn.getMonth(cal));
        assertEquals((Integer) 25, DateColumn.getDayOfMonth(cal));
        assertEquals((Integer) 16, DateColumn.getHour(cal));
        assertEquals((Integer) 15, DateColumn.getMinutes(cal));
        assertEquals((Integer) 12, DateColumn.getSeconds(cal));
        assertEquals((Integer) 123, DateColumn.getMilliseconds(cal));
        assertNull(DateColumn.getYear(null));
        assertNull(DateColumn.getMonth(null));
        assertNull(DateColumn.getDayOfMonth(null));
        assertNull(DateColumn.getHour(null));
        assertNull(DateColumn.getMinutes(null));
        assertNull(DateColumn.getSeconds(null));
        assertNull(DateColumn.getMilliseconds(null));
    }

    @Test
    public void formatDate() throws Exception {
        Date cal = getDate(2017, 2, 25, 16, 15, 12, 123);
        DateColumn dc = new DateColumn("created", "dd.MM.YYYY HH.mm.ss");
        assertEquals("25.03.2017 16.15.12", dc.formatDate(cal));
        assertEquals("", dc.formatDate(null));
    }

}