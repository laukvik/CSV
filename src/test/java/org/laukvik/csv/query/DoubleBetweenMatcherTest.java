package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.columns.DoubleColumn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DoubleBetweenMatcherTest {

    @Test
    public void isBetween() throws Exception {
        assertTrue(DoubleBetweenMatcher.isBetween(100d, 100d, 200d));
        assertTrue(DoubleBetweenMatcher.isBetween(199d, 100d, 200d));
        assertFalse(DoubleBetweenMatcher.isBetween(99d, 100d, 200d));
        assertFalse(DoubleBetweenMatcher.isBetween(null, 100d, 200d));
    }

    @Test
    public void getColumn() throws Exception {
        DoubleColumn c = new DoubleColumn("value");
        DoubleBetweenMatcher m = new DoubleBetweenMatcher(c, 100d, 200d);
        assertEquals(c, m.getColumn());
    }

    @Test
    public void matches() throws Exception {
        DoubleColumn c = new DoubleColumn("value");
        DoubleBetweenMatcher m = new DoubleBetweenMatcher(c, 100d, 200d);
        assertTrue(m.matches(100d));
        assertTrue(m.matches(199d));
        assertFalse(m.matches(200d));
    }

}