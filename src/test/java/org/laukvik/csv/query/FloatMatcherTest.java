package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.FloatColumn;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FloatMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        FloatColumn c = csv.addFloatColumn("value");
        Row r1 = csv.addRow().setFloat(c, 20f);
        Row r2 = csv.addRow().setFloat(c, 30f);
        Row r3 = csv.addRow().setFloat(c, 40f);

        FloatMatcher m = new FloatMatcher(c, 20f);
        assertTrue(m.matches(r1));
        assertFalse(m.matches(r2));
        assertFalse(m.matches(r3));
    }

}