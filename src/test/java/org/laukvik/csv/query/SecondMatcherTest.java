package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.DateColumn;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SecondMatcherTest {

    @Test
    public void getColumn() {
        DateColumn c = new DateColumn("value");
        SecondMatcher m = new SecondMatcher(c, 11);
        assertEquals(c, m.getColumn());
    }

    @Test
    public void matches() throws Exception {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(Calendar.SECOND, 10);
        Date d1 = cal.getTime();
        cal.set(Calendar.SECOND, 11);
        Date d2 = cal.getTime();
        cal.set(Calendar.SECOND, 12);
        Date d3 = cal.getTime();

        CSV csv = new CSV();
        DateColumn c = csv.addDateColumn("value");

        SecondMatcher m = new SecondMatcher(c, 11);
        assertFalse(m.matches(d1));
        assertTrue(m.matches(d2));
        assertFalse(m.matches(d3));
        assertFalse(m.matches(null));
    }
}