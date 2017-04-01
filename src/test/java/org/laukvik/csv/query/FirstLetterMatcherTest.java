package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.StringColumn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FirstLetterMatcherTest {

    @Test
    public void getColumn() {
        StringColumn c = new StringColumn("value");
        FirstLetterMatcher m = new FirstLetterMatcher(c);
        assertEquals(c, m.getColumn());
    }

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        StringColumn c = csv.addStringColumn("value");

        FirstLetterMatcher m = new FirstLetterMatcher(c, "A");
        assertTrue(m.matches("Alpha"));
        assertFalse(m.matches("Beta"));
        assertFalse(m.matches("gamma"));
        assertFalse(m.matches(null));
    }

}