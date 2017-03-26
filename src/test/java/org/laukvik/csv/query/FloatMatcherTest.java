package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.FloatColumn;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FloatMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        FloatColumn c = csv.addFloatColumn("value");

        FloatMatcher m = new FloatMatcher(c, 20f);
        assertTrue(m.matches(20f));
        assertFalse(m.matches(30f));
        assertFalse(m.matches(40f));
        assertFalse(m.matches(null));
    }

}