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
public class IntIsInMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        IntegerColumn first = csv.addIntegerColumn("first");
        Row r1 = csv.addRow().setInteger(first, 12);
        Row r2 = csv.addRow();
        Row r3 = csv.addRow().setInteger(first, 10);
        IntIsInMatcher m = new IntIsInMatcher(first, 12, 13, 14);
        assertTrue(m.matches(12));
        assertFalse(m.matches(null));
        assertFalse(m.matches(10));

        Integer i = null;
        IntIsInMatcher m2 = new IntIsInMatcher(first, i);
        assertFalse(m2.matches(12));
        assertTrue(m2.matches(null));
        assertFalse(m2.matches(10));

    }

}