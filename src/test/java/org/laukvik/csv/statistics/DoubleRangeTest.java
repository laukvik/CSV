package org.laukvik.csv.statistics;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class DoubleRangeTest {

    @Test
    public void addValue() throws Exception {
        DoubleRange r = new DoubleRange("test", -0.5, 0.5);
        r.addValue(0.51);
        assertEquals(0, r.count);
        r.addValue(-0.51);
        assertEquals(0, r.count);

        r.addValue(-0.50);
        assertEquals(1, r.count);
        r.addValue(0.50);
        assertEquals(1, r.count);
    }

    @Test
    public void contains() throws Exception {
        DoubleRange r = new DoubleRange("test", -0.3, 0.53);
        assertFalse(r.contains(0.531));
        assertFalse(r.contains(-0.31));
    }

}