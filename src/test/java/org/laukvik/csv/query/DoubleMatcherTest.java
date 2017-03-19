package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.DoubleColumn;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DoubleMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        DoubleColumn c = csv.addDoubleColumn("value");
        Row r1 = csv.addRow().setDouble(c, 100d);
        Row r2 = csv.addRow().setDouble(c, 200d);
        Row r3 = csv.addRow().setDouble(c, 300d);
        DoubleMatcher m = new DoubleMatcher(c, 200d);
        assertFalse(m.matches(r1));
        assertTrue(m.matches(r2));
        assertFalse(m.matches(r3));
    }

}