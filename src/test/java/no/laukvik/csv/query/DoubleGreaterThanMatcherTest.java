package no.laukvik.csv.query;

import no.laukvik.csv.columns.DoubleColumn;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DoubleGreaterThanMatcherTest {

    @Test
    public void isGreaterThan() throws Exception {
        assertTrue(DoubleGreaterThanMatcher.isGreaterThan(101d, 100d));
    }

    @Test
    public void getColumn() throws Exception {
        DoubleColumn c = new DoubleColumn("value");
        DoubleGreaterThanMatcher m = new DoubleGreaterThanMatcher(c, 100d);
        assertEquals(c, m.getColumn());
    }

    @Test
    public void matches() throws Exception {
        DoubleColumn c = new DoubleColumn("value");
        DoubleGreaterThanMatcher m = new DoubleGreaterThanMatcher(c, 100d);
        assertTrue(m.matches(100.1d));
        assertTrue(m.matches(102d));
        assertFalse(m.matches(100d));
        assertFalse(m.matches(null));
    }

}