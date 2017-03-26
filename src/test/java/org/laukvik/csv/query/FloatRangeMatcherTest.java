package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.FloatColumn;
import org.laukvik.csv.statistics.FloatRange;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FloatRangeMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        FloatColumn c = csv.addFloatColumn("value");

        FloatRange fr = new FloatRange("untitled", 25f, 45f);

        FloatRangeMatcher m = new FloatRangeMatcher(c, fr);
        assertFalse(m.matches(20f));
        assertTrue(m.matches(30f));
        assertTrue(m.matches(40f));
    }

}