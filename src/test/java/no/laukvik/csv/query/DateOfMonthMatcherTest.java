package no.laukvik.csv.query;

import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.DateColumn;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DateOfMonthMatcherTest {

    @Test
    public void getColumn() {
        DateColumn c = new DateColumn("value");
        DateOfMonthMatcher m = new DateOfMonthMatcher(c);
        assertEquals(c, m.getColumn());
    }

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        DateColumn c = csv.addDateColumn("visible");

        GregorianCalendar cal = new GregorianCalendar();
        cal.set(Calendar.DAY_OF_MONTH, 5);
        Date d5 = cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH, 6);
        Date d6 = cal.getTime();
        Date d7 = null;


        DateOfMonthMatcher m1 = new DateOfMonthMatcher(c, 5);
        assertTrue(m1.matches(d5));
        assertFalse(m1.matches(d6));
        assertFalse(m1.matches(d7));

        assertTrue(m1.matches(d5));
        assertFalse(m1.matches(d6));
        assertFalse(m1.matches(d7));

//        assertTrue(m1.matchesRow(d7));
        assertFalse(m1.matches(d7));
    }

    @Test
    public void matchesDate() throws Exception {
        DateColumn c = new DateColumn("test");

        GregorianCalendar cal = new GregorianCalendar();
        cal.set(Calendar.DAY_OF_MONTH, 5);
        Date d5 = cal.getTime();
        Date d7 = null;

        DateOfMonthMatcher m1 = new DateOfMonthMatcher(c, 1, null);
        assertFalse(m1.matches(d5));
        assertTrue(m1.matches(d7));
    }


}