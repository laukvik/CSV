package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.columns.FloatColumn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FloatBetweenMatcherTest {

    @Test
    public void isBetween() throws Exception {
        assertTrue(FloatBetweenMatcher.isBetween(100f, 100f, 200f));
        assertTrue(FloatBetweenMatcher.isBetween(199f, 100f, 200f));
        assertFalse(FloatBetweenMatcher.isBetween(99f, 100f, 200f));
    }

    @Test
    public void getColumn() throws Exception {
        FloatColumn c = new FloatColumn("value");
        FloatBetweenMatcher m = new FloatBetweenMatcher(c, 100f, 200f);
        assertEquals(c, m.getColumn());
    }

    @Test
    public void matches() throws Exception {
        FloatColumn c = new FloatColumn("value");
        FloatBetweenMatcher m = new FloatBetweenMatcher(c, 100f, 200f);
        assertTrue(m.matches(100f));
        assertTrue(m.matches(199f));
        assertFalse(m.matches(200f));
        assertFalse(m.matches(null));
    }

}