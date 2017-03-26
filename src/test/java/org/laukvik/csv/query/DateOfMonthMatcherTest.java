package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.DateColumn;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DateOfMonthMatcherTest {

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

        DateOfMonthMatcher m1 = new DateOfMonthMatcher(c, 1,null);
        assertFalse(m1.matches(d5));
        assertTrue(m1.matches(d7));
    }



}