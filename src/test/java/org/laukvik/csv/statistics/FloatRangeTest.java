package org.laukvik.csv.statistics;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FloatRangeTest {

    @Test
    public void addValue() throws Exception {
        FloatRange r = new FloatRange("test", -0.5f, 1.5f);
        r.addValue(-0.51f);
        assertEquals(0, r.count);
        r.addValue(1.51f);
        assertEquals(0, r.count);

        r.addValue(-0.5f);
        assertEquals(1, r.count);
        r.addValue(1.5f);
        assertEquals(2, r.count);
    }

    @Test
    public void contains() throws Exception {
        FloatRange r = new FloatRange("test", -0.2f, 1.2f);
        assertTrue(r.contains(-0.2f));
        assertTrue(r.contains(1.2f));
        assertFalse(r.contains(1.21f));
        assertFalse(r.contains(-0.21f));
    }

}