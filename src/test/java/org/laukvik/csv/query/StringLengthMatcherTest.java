package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.StringColumn;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class StringLengthMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("first");
        Row r1 = csv.addRow().setString(first, "One");
        Row r2 = csv.addRow().setString(first, "Two");
        Row r3 = csv.addRow().setString(first, "Three");
        Row r4 = csv.addRow();
        StringLengthMatcher m = new StringLengthMatcher(first, 3);
        assertTrue(m.matches(r1));
        assertTrue(m.matches(r2));
        assertFalse(m.matches(r3));
        assertFalse(m.matches(r4));
    }

}