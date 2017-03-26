package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.IntegerColumn;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 */
public class IntegerGreaterThanMatcherTest {

    @Test
    public void isGreaterThan() throws Exception {
        assertTrue(IntegerGreaterThanMatcher.isGreaterThan(5, 0));
        assertFalse(IntegerGreaterThanMatcher.isGreaterThan(0, 0));
        assertFalse(IntegerGreaterThanMatcher.isGreaterThan(null, 0));
        assertFalse(IntegerGreaterThanMatcher.isGreaterThan(-1, 0));
    }

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        IntegerColumn first = csv.addIntegerColumn("last");
        IntegerIsInMatcher m = new IntegerIsInMatcher(first, 12, 13, 14);
        assertTrue(m.matches(12));
        assertFalse(m.matches(null));
    }

}