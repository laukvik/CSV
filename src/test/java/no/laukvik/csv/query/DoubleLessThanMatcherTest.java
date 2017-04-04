package no.laukvik.csv.query;

import no.laukvik.csv.columns.DoubleColumn;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DoubleLessThanMatcherTest {

    @Test
    public void getColumn() throws Exception {
        DoubleColumn c = new DoubleColumn("value");
        DoubleLessThanMatcher m = new DoubleLessThanMatcher(c, 100f);
        Assert.assertEquals(c, m.getColumn());
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