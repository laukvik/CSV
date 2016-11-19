package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.IntegerColumn;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 */
public class IntBetweenMatcherTest {

    @Test
    public void isBetween() throws Exception {
        assertTrue(IntBetweenMatcher.isBetween(5, 0, 10));
        assertTrue(IntBetweenMatcher.isBetween(0, 0, 10));
        assertTrue(IntBetweenMatcher.isBetween(10, 0, 10));
        assertFalse(IntBetweenMatcher.isBetween(-1, 0, 10));
        assertFalse(IntBetweenMatcher.isBetween(11, 0, 10));
    }

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        IntegerColumn first = csv.addIntegerColumn("first");
        Row r1 = csv.addRow().setInteger(first, 1);
        Row r2 = csv.addRow().setInteger(first, 3);
        Row r3 = csv.addRow().setInteger(first, 5);
        Row r4 = csv.addRow().setInteger(first, 5);
        Row r5 = csv.addRow();
        IntBetweenMatcher m = new IntBetweenMatcher(first, 1, 4);
        assertTrue(m.matches(r1));
        assertTrue(m.matches(r2));
        assertFalse(m.matches(r3));
        assertFalse(m.matches(r4));
        assertFalse(m.matches(r5));
    }

}