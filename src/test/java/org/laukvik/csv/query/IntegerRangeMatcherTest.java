package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.IntegerColumn;
import org.laukvik.csv.statistics.IntegerRange;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IntegerRangeMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        IntegerColumn c = csv.addIntegerColumn("value");
        Row r1 = csv.addRow().setInteger(c, 100);
        Row r2 = csv.addRow().setInteger(c, 200);
        Row r3 = csv.addRow().setInteger(c, 300);

        IntegerRange dr1 = new IntegerRange("", 0,250);

        IntegerRangeMatcher m = new IntegerRangeMatcher(c, dr1);
        assertTrue(m.matches(r1));
        assertTrue(m.matches(r2));
        assertFalse(m.matches(r3));
    }

}