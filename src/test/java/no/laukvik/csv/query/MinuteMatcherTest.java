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

public class MinuteMatcherTest {

    @Test
    public void getColumn() {
        DateColumn c = new DateColumn("value");
        MinuteMatcher m = new MinuteMatcher(c);
        assertEquals(c, m.getColumn());
    }

    @Test
    public void matches() throws Exception {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(Calendar.MINUTE, 10);
        Date d1 = cal.getTime();
        cal.set(Calendar.MINUTE, 11);
        Date d2 = cal.getTime();
        cal.set(Calendar.MINUTE, 12);
        Date d3 = cal.getTime();

        CSV csv = new CSV();
        DateColumn c = csv.addDateColumn("value");

        MinuteMatcher m = new MinuteMatcher(c, 11);
        assertFalse(m.matches(d1));
        assertTrue(m.matches(d2));
        assertFalse(m.matches(d3));
        assertFalse(m.matches(null));
    }

}