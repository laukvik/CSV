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
        IntIsInMatcher m = new IntIsInMatcher(first, 12, 13, 14);
        assertTrue(m.matches(r1));
        assertFalse(m.matches(r2));
    }

}