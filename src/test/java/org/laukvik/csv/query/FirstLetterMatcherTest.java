package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.StringColumn;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FirstLetterMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        StringColumn c = csv.addStringColumn("value");
        Row r1 = csv.addRow().setString(c, "Alpha");
        Row r2 = csv.addRow().setString(c, "Beta");
        Row r3 = csv.addRow().setString(c, "gamma");

        FirstLetterMatcher m = new FirstLetterMatcher(c, "A");
        assertTrue(m.matches(r1));
        assertFalse(m.matches(r2));
        assertFalse(m.matches(r3));
    }

}