package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.DateColumn;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 */
public class DateGreaterThanMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        DateColumn first = csv.addDateColumn("created", "yyyy.MM.dd");
        Row r1 = csv.addRow().set(first, first.parse("2002.05.05"));
        Row r2 = csv.addRow().set(first, first.parse("2003.05.05"));
        Row r3 = csv.addRow().set(first, first.parse("2004.05.05"));
        Row r4 = csv.addRow().set(first, null);

        DateGreaterThanMatcher m = new DateGreaterThanMatcher(first, first.parse("2003.05.05"));

        assertFalse( m.matches(r1) );
        assertFalse( m.matches(r2) );
        assertTrue( m.matches(r3) );
        assertFalse( m.matches(r4) );
    }

}