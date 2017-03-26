package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.DoubleColumn;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DoubleMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        DoubleColumn c = csv.addDoubleColumn("value");
        DoubleMatcher m = new DoubleMatcher(c, 200d);
        assertFalse(m.matches(100d));
        assertTrue(m.matches(200d));
        assertFalse(m.matches(300d));
        assertFalse(m.matches(null));
    }

}