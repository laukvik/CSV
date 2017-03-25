package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.DateColumn;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MonthMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        DateColumn created = csv.addDateColumn("created", "yyyy.MM.dd");
        Row r1 = csv.addRow().setDate(created, created.parse("2005.1.1"));
        Row r2 = csv.addRow().setDate(created, created.parse("2005.2.1"));
        Row r3 = csv.addRow().setDate(created, created.parse("2005.3.1"));
        Row r4 = csv.addRow().setDate(created, null);
        Row r5 = csv.addRow().setDate(created, created.parse("2005.4.1"));
        Row r6 = csv.addRow().setDate(created, created.parse("2005.5.1"));
        MonthMatcher m = new MonthMatcher(created, 0,1,2,null);
        assertTrue(m.matches(r1));
        assertTrue(m.matches(r2));
        assertTrue(m.matches(r3));
        assertTrue(m.matches(r4));
        assertFalse(m.matches(r5));
        assertFalse(m.matches(r6));
    }

}