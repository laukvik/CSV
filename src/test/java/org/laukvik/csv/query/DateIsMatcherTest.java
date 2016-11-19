package org.laukvik.csv.query;

import org.junit.Assert;
import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.DateColumn;

import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertFalse;

/**
 * @author Morten Laukvik
 */
public class DateIsMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        DateColumn first = csv.addDateColumn("created");
        Row r = csv.addRow();
        DateIsMatcher m = new DateIsMatcher(first, new Date());
        assertFalse( m.matches(r) );
        DateIsMatcher m2 = new DateIsMatcher(first, null);
        assertFalse( m2.matches(r) );
    }


    @Test
    public void isEqual() throws Exception {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(1951, 3, 22);
        Date d1 = cal.getTime();
        Date d2 = cal.getTime();
        Assert.assertTrue(DateColumn.isEqualDate(d1, d2));
    }

    @Test
    public void isEqualFail() throws Exception {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(1951, 3, 21);
        Date d1 = cal.getTime();
        cal.set(1951, 3, 22);
        Date d2 = cal.getTime();
        Assert.assertTrue(!DateColumn.isEqualDate(d1, d2));
    }

}