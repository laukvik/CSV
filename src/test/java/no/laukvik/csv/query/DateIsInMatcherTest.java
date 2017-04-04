package no.laukvik.csv.query;

import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.DateColumn;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 */
public class DateIsInMatcherTest {

    @Test
    public void matches() throws Exception {
        Calendar c = new GregorianCalendar();
        c.set(2002, 3, 1, 0, 0, 0);
        Date d2002 = c.getTime();

        c.set(2003, 5, 3, 0, 0, 0);
        Date d2003 = c.getTime();

        c.set(2005, 10, 7, 0, 0, 0);
        Date d2005 = c.getTime();

        CSV csv = new CSV();
        DateColumn created = csv.addDateColumn("created");

        DateIsInMatcher matcher = new DateIsInMatcher(created, d2002, d2003, d2005);
        assertTrue(matcher.matches(d2003));

        matcher = new DateIsInMatcher(created);
        assertFalse(matcher.matches(d2003));

        matcher = new DateIsInMatcher(created, d2002, d2005);
        assertFalse(matcher.matches(d2003));

        matcher = new DateIsInMatcher(created, d2005, d2003);
        assertTrue(matcher.matches(d2003));

    }

}