package org.laukvik.csv.columns;

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

    private Date getTomorrow() {
        Calendar cal = new GregorianCalendar();
        Date today = cal.getTime();
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
    }

    @Test
    public void constructor() throws Exception {
        DateColumn c = new DateColumn("created");
        assertEquals("created", c.getName());
        assertEquals(DateColumn.DEFAULT_DATE_FORMAT, c.getFormat());
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
//        assertEquals(0, c.compare(tomorrow, cal.getTime()));
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
    }

    @Test
    public void isEqualTime() throws Exception {
        Date tomorrow = getTomorrow();
        assertTrue(DateColumn.isEqualTime(tomorrow, tomorrow));
        assertFalse(DateColumn.isEqualTime(tomorrow, null));
        assertFalse(DateColumn.isEqualTime(null, tomorrow));
        assertTrue(DateColumn.isEqualTime(null, null));
    }

    @Test
    public void isYear() throws Exception {
        Calendar cal = new GregorianCalendar();
        Date today = cal.getTime();
        assertTrue(DateColumn.isYear(today, cal.get(Calendar.YEAR)));
        assertFalse(DateColumn.isYear(null, cal.get(Calendar.YEAR)));
    }

    @Test
    public void isYearGreaterThan() throws Exception {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.YEAR, 1);
        assertFalse(DateColumn.isYearGreaterThan(new Date(), cal.get(Calendar.YEAR)));
        assertFalse(DateColumn.isYearGreaterThan(null, cal.get(Calendar.YEAR)));
//        assertTrue(DateColumn.isYearGreaterThan(new Date(), cal.get(Calendar.YEAR) - 1));
        assertFalse(DateColumn.isYearGreaterThan(new Date(), cal.get(Calendar.YEAR)));
    }

    @Test
    public void isYearLessThan() throws Exception {
        Calendar cal = new GregorianCalendar();
        assertFalse(DateColumn.isYearLessThan(new Date(), cal.get(Calendar.YEAR)));

        cal.add(Calendar.YEAR, -1);
        assertFalse(DateColumn.isYearLessThan(new Date(), cal.get(Calendar.YEAR)));

        cal.add(Calendar.YEAR, 5);
        assertTrue(DateColumn.isYearLessThan(new Date(), cal.get(Calendar.YEAR)));
    }

    @Test
    public void isYearBetween() throws Exception {
        Calendar cal = new GregorianCalendar();
        int year = cal.get(Calendar.YEAR);
        cal.add(Calendar.YEAR, -1);
        int yearFrom = cal.get(Calendar.YEAR);
        cal.add(Calendar.YEAR, 1);
        int yearTo = cal.get(Calendar.YEAR);
        assertTrue(DateColumn.isYearBetween(new Date(), yearFrom, yearTo));

        assertFalse(DateColumn.isYearBetween(new Date(), yearTo, yearFrom));
        assertTrue(DateColumn.isYearBetween(new Date(), year, year));
        assertFalse(DateColumn.isYearBetween(new Date(), year, yearFrom));
        assertTrue(DateColumn.isYearBetween(new Date(), yearFrom, year));
    }

    @Test
    public void isMonth() throws Exception {
        Calendar cal = new GregorianCalendar();
        int month = cal.get(Calendar.MONTH);
        assertTrue(DateColumn.isMonth(new Date(), month));
        assertFalse(DateColumn.isMonth(new Date(), month+1));
        assertFalse(DateColumn.isMonth(new Date(), month-1));
    }

    @Test
    public void isWeek() throws Exception {
        Calendar cal = new GregorianCalendar();
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        assertTrue(DateColumn.isWeek(new Date(), week));
        assertFalse(DateColumn.isWeek(new Date(), week+1));
        assertFalse(DateColumn.isWeek(new Date(), week-1));
    }

    @Test
    public void isDayOfWeek() throws Exception {
        Calendar cal = new GregorianCalendar();
        int dow = cal.get(Calendar.DAY_OF_WEEK);
        assertTrue(DateColumn.isDayOfWeek(new Date(), dow));
        assertFalse(DateColumn.isDayOfWeek(new Date(), dow+1));
        assertFalse(DateColumn.isDayOfWeek(new Date(), dow-1));
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
        cal.set(2000,11,24, 18, 30, 0);
        assertEquals("24.12.2000 18.30.00", dc.asString(cal.getTime()));
    }

    @Test
    public void parse() throws Exception {
        DateColumn c = new DateColumn("created", "yyyy-MM-dd HH:mm:ss");
        Calendar cal = new GregorianCalendar();
        String dateString = "2000-01-01 00:00:00";
        Date d = c.parse(dateString);
        assertEquals(dateString, c.asString(d));
        assertNull(dateString, c.parse(""));
        assertNull(dateString, c.parse(null));
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
        Date tomorrow = getTomorrow();
        assertEquals(-1, DateColumn.compareDates(today, tomorrow));
        assertEquals(1, DateColumn.compareDates(tomorrow, today));
        assertEquals(0, DateColumn.compareDates(tomorrow, cal.getTime()));
        assertEquals(1, DateColumn.compareDates(tomorrow, null));
        assertEquals(-1, DateColumn.compareDates(null, cal.getTime()));
        assertEquals(0, DateColumn.compareDates(null, null));
    }

}