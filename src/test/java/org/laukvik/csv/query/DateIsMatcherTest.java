package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.DateColumn;

import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertTrue;

/**
 * @author Morten Laukvik
 */
public class DateIsMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        DateColumn first = csv.addDateColumn("created", "yyyy.MM.dd");
        Row r1 = csv.addRow().setDate(first, first.parse("2010.01.01"));
        Row r2 = csv.addRow();
        Row r3 = csv.addRow().setDate(first, first.parse("2010.03.01"));
        DateIsMatcher m = new DateIsMatcher(first, first.parse("2010.01.01"));
        assertTrue( m.matches(r1) );
    }


    @Test
    public void isEqual() throws Exception {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(1951, 3, 22);
        Date d1 = cal.getTime();
        Date d2 = cal.getTime();
        assertTrue(DateColumn.isEqualDate(d1, d2));
    }

    @Test
    public void isEqualFail() throws Exception {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(1951, 3, 21);
        Date d1 = cal.getTime();
        cal.set(1951, 3, 22);
        Date d2 = cal.getTime();
        assertTrue(!DateColumn.isEqualDate(d1, d2));
    }

}