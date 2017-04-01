package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.columns.DoubleColumn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DoubleLessThanMatcherTest {

    @Test
    public void getColumn() throws Exception {
        DoubleColumn c = new DoubleColumn("value");
        DoubleLessThanMatcher m = new DoubleLessThanMatcher(c, 100f);
        assertEquals(c, m.getColumn());
    }

    @Test
    public void matches() throws Exception {
        DoubleColumn c = new DoubleColumn("value");
        DoubleLessThanMatcher m = new DoubleLessThanMatcher(c, 100d);
        assertTrue(m.matches(99.9d));
        assertTrue(m.matches(50d));
        assertFalse(m.matches(100d));
        assertFalse(m.matches(null));
    }

}