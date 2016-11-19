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

/**
 */
public class DateIsInMatcherTest {

    @Test
    public void matches() throws Exception {
        Calendar c = new GregorianCalendar();
        c.set(2002, 3, 1, 0,0,0);
        Date d2002 = c.getTime();

        c.set(2003, 5, 3, 0,0,0);
        Date d2003 = c.getTime();

        c.set(2005, 10, 7, 0,0,0);
        Date d2005 = c.getTime();

        CSV csv = new CSV();
        DateColumn created = csv.addDateColumn("created");
        Row row = csv.addRow().setDate(created, d2003);

        DateIsInMatcher matcher = new DateIsInMatcher(created, d2002, d2003, d2005);
        assertTrue( matcher.matches(row) );

        matcher = new DateIsInMatcher(created);
        assertFalse( matcher.matches(row) );

        matcher = new DateIsInMatcher(created, d2002, d2005);
        assertFalse( matcher.matches(row) );

        matcher = new DateIsInMatcher(created, d2005, d2003);
        assertTrue( matcher.matches(row) );

    }

}