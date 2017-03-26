package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.DateColumn;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class YearMatcherTest {

    @Test
    public void matches() throws Exception {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, 2010);
        Date d1 = cal.getTime();
        cal.set(Calendar.YEAR, 2011);
        Date d2 = cal.getTime();
        cal.set(Calendar.YEAR, 2012);
        Date d3 = cal.getTime();

        CSV csv = new CSV();
        DateColumn c = csv.addDateColumn("value");

        YearMatcher m = new YearMatcher(c, 2011);
        assertFalse(m.matches(d1));
        assertTrue(m.matches(d2));
        assertFalse(m.matches(d3));
    }

}