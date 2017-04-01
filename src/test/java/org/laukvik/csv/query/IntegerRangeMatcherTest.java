package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.IntegerColumn;
import org.laukvik.csv.statistics.IntegerRange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IntegerRangeMatcherTest {

    @Test
    public void getColumn() {
        IntegerColumn c = new IntegerColumn("value");
        IntegerRangeMatcher m = new IntegerRangeMatcher(c);
        assertEquals(c, m.getColumn());
    }

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        IntegerColumn c = csv.addIntegerColumn("value");
        IntegerRange dr1 = new IntegerRange("", 0,250);
        IntegerRangeMatcher m = new IntegerRangeMatcher(c, dr1);
        assertTrue(m.matches(100));
        assertTrue(m.matches(200));
        assertFalse(m.matches(300));
        assertFalse(m.matches(null));
    }

}