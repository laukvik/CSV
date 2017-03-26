package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.StringColumn;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class StringLengthMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("first");
        StringLengthMatcher m = new StringLengthMatcher(first, 3);
        assertTrue(m.matches("One"));
        assertTrue(m.matches("Two"));
        assertFalse(m.matches("Three"));
        assertFalse(m.matches(null));
    }

}