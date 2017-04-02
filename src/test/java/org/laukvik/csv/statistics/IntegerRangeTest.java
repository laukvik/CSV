package org.laukvik.csv.statistics;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IntegerRangeTest {

    @Test
    public void addValue() throws Exception {
        IntegerRange r = new IntegerRange("Test", -5, 5);
        r.addValue(6);
        assertEquals(0, r.count);
        r.addValue(-6);
        assertEquals(0, r.count);

        r.addValue(4);
        assertEquals(1, r.count);
        r.addValue(-5);
        assertEquals(2, r.count);
    }

    @Test
    public void contains() throws Exception {
        IntegerRange r = new IntegerRange("Test", -5, 5);
        assertTrue( r.contains(-5) );
        assertTrue(r.contains(4));
        assertFalse( r.contains(6) );
        assertFalse( r.contains(-6) );
    }

}