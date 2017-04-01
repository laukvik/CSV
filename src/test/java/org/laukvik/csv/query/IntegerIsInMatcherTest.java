package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.IntegerColumn;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class IntegerIsInMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        IntegerColumn first = csv.addIntegerColumn("first");
        Row r1 = csv.addRow().set(first, 12);
        Row r2 = csv.addRow();
        Row r3 = csv.addRow().set(first, 10);
        IntegerIsInMatcher m = new IntegerIsInMatcher(first, 12, 13, 14);
        assertTrue(m.matches(12));
        assertFalse(m.matches(null));
        assertFalse(m.matches(10));

        Integer i = null;
        IntegerIsInMatcher m2 = new IntegerIsInMatcher(first, i);
        assertFalse(m2.matches(12));
        assertTrue(m2.matches(null));
        assertFalse(m2.matches(10));

    }

}