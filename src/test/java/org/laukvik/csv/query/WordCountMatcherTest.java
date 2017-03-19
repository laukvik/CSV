package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.StringColumn;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WordCountMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        StringColumn c = csv.addStringColumn("value");
        Row r1 = csv.addRow().setString(c, "");
        Row r2 = csv.addRow().setString(c, null);
        Row r3 = csv.addRow().setString(c, " The red fox.");
        Row r4 = csv.addRow().setString(c, "The red fox lazy dog. ");
        Row r5 = csv.addRow().setString(c, "   The red fox     dog    ");
        Row r6 = csv.addRow().setString(c, "The red dog.");
        Row r7 = csv.addRow().setString(c, "The lazy dog.");

        WordCountMatcher m = new WordCountMatcher(c, 3);
        assertFalse(m.matches(r1));
        assertFalse(m.matches(r2));
        assertTrue(m.matches(r3));
        assertFalse(m.matches(r4));
        assertFalse(m.matches(r5));
        assertTrue(m.matches(r6));
        assertTrue(m.matches(r7));
    }

}