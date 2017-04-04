package no.laukvik.csv.query;

import no.laukvik.csv.columns.FloatColumn;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FloatGreaterThanMatcherTest {

    @Test
    public void isGreaterThan() throws Exception {
        assertTrue(FloatGreaterThanMatcher.isGreaterThan(101f, 100f));
    }

    @Test
    public void getColumn() throws Exception {
        FloatColumn c = new FloatColumn("value");
        FloatGreaterThanMatcher m = new FloatGreaterThanMatcher(c, 100f);
        assertEquals(c, m.getColumn());
    }

    @Test
    public void matches() throws Exception {
        FloatColumn c = new FloatColumn("value");
        FloatGreaterThanMatcher m = new FloatGreaterThanMatcher(c, 100f);
        assertTrue(m.matches(100.1f));
        assertTrue(m.matches(102f));
        assertFalse(m.matches(100f));
        assertFalse(m.matches(null));
    }

}