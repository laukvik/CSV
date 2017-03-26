package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.StringColumn;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 */
public class AnyMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        StringColumn c1 = new StringColumn("first");

        Row r1 = csv.addRow().setString(c1, "Steve");
        Row r2 = csv.addRow().setString(c1, "Bill");
        Row r3 = csv.addRow().setString(c1, "John");
        Row r4 = csv.addRow().setString(c1, "James");

        RegExMatcher m1 = new RegExMatcher(c1, "Steve");
        RegExMatcher m2 = new RegExMatcher(c1, "Bill");

        AnyMatcher m = new AnyMatcher(m1, m2);

        assertTrue(m.matchesRow(r1));
        assertTrue(m.matchesRow(r2));
        assertFalse(m.matchesRow(r3));
        assertFalse(m.matchesRow(r4));

    }

}