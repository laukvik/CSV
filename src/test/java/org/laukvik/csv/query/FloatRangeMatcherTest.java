package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.FloatColumn;
import org.laukvik.csv.statistics.FloatRange;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FloatRangeMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        FloatColumn c = csv.addFloatColumn("value");
        Row r1 = csv.addRow().setFloat(c, 20f);
        Row r2 = csv.addRow().setFloat(c, 30f);
        Row r3 = csv.addRow().setFloat(c, 40f);

        FloatRange fr = new FloatRange("untitled", 25f, 45f);

        FloatRangeMatcher m = new FloatRangeMatcher(c, fr);
        assertFalse(m.matches(r1));
        assertTrue(m.matches(r2));
        assertTrue(m.matches(r3));
    }

}