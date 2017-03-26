package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.DoubleColumn;
import org.laukvik.csv.statistics.DoubleRange;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DoubleRangeMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        DoubleColumn c = csv.addDoubleColumn("value");
        Row r1 = csv.addRow().setDouble(c, 100d);
        Row r2 = csv.addRow().setDouble(c, 200d);
        Row r3 = csv.addRow().setDouble(c, 300d);

        DoubleRange dr1 = new DoubleRange("", 0d,250d);

        DoubleRangeMatcher m = new DoubleRangeMatcher(c, dr1);
        assertTrue(m.matches(r1));
        assertTrue(m.matches(r2));
        assertFalse(m.matches(r3));
    }

}