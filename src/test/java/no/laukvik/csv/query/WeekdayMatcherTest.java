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

public class WeekdayMatcherTest {

    @Test
    public void getColumn() {
        DateColumn c = new DateColumn("value");
        WeekdayMatcher m = new WeekdayMatcher(c, 11);
        assertEquals(c, m.getColumn());
    }

    @Test
    public void matches() throws Exception {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(Calendar.DAY_OF_WEEK, 1);
        Date d1 = cal.getTime();
        cal.set(Calendar.DAY_OF_WEEK, 2);
        Date d2 = cal.getTime();
        cal.set(Calendar.DAY_OF_WEEK, 3);
        Date d3 = cal.getTime();

        CSV csv = new CSV();
        DateColumn c = csv.addDateColumn("value");

        WeekdayMatcher m = new WeekdayMatcher(c, 2);
        assertFalse(m.matches(d1));
        assertTrue(m.matches(d2));
        assertFalse(m.matches(d3));
        assertFalse(m.matches(null));
    }

}