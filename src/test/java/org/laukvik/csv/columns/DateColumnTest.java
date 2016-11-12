package org.laukvik.csv.columns;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
    public void compareDates() throws Exception {
        DateColumn c = new DateColumn("created");
        Calendar cal = new GregorianCalendar();
        Date today = cal.getTime();
        cal.add(Calendar.DATE, 1);
        Date tomorrow = getTomorrow();
        assertEquals(-1, c.compare(today, tomorrow));
        assertEquals(1, c.compare(tomorrow, today));
        assertEquals(0, c.compare(tomorrow, cal.getTime()));
    }

    @Test
    public void isLessThan() throws Exception {
        Date tomorrow = getTomorrow();
        assertTrue(DateColumn.isGreaterThan(tomorrow, new Date()));
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

    }

    @Test
    public void isYearBetween() throws Exception {

    }

    @Test
    public void isMonth() throws Exception {

    }

    @Test
    public void isWeek() throws Exception {

    }

    @Test
    public void isDayOfWeek() throws Exception {

    }

    @Test
    public void getFormat() throws Exception {

    }

    @Test
    public void getDateFormat() throws Exception {

    }

    @Test
    public void asString() throws Exception {

    }

    @Test
    public void parse() throws Exception {
        DateColumn c = new DateColumn("created", "yyyy-MM-dd HH:mm:ss");
        Calendar cal = new GregorianCalendar();
        String dateString = "2000-01-01 00:00:00";
        Date d = c.parse(dateString);
        assertEquals(dateString, c.asString(d));
    }


}