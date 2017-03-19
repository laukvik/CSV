package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.DateColumn;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WeekMatcherTest {

    @Test
    public void matches() throws Exception {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(Calendar.WEEK_OF_YEAR, 10);
        Date d1 = cal.getTime();
        cal.set(Calendar.WEEK_OF_YEAR, 11);
        Date d2 = cal.getTime();
        cal.set(Calendar.WEEK_OF_YEAR, 12);
        Date d3 = cal.getTime();

        CSV csv = new CSV();
        DateColumn c = csv.addDateColumn("value");
        Row r1 = csv.addRow().setDate(c, d1);
        Row r2 = csv.addRow().setDate(c, d2);
        Row r3 = csv.addRow().setDate(c, d3);

        WeekMatcher m = new WeekMatcher(c, 11);
        assertFalse(m.matches(r1));
        assertTrue(m.matches(r2));
        assertFalse(m.matches(r3));
    }

}