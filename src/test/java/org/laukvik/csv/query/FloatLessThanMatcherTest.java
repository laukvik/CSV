package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.columns.FloatColumn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FloatLessThanMatcherTest {

    @Test
    public void getColumn() throws Exception {
        FloatColumn c = new FloatColumn("value");
        FloatLessThanMatcher m = new FloatLessThanMatcher(c, 100f);
        assertEquals(c, m.getColumn());
    }

    @Test
    public void matches() throws Exception {
        FloatColumn c = new FloatColumn("value");
        FloatLessThanMatcher m = new FloatLessThanMatcher(c, 100f);
        assertTrue(m.matches(99.9f));
        assertTrue(m.matches(50f));
        assertFalse(m.matches(100f));
        assertFalse(m.matches(null));
    }

}