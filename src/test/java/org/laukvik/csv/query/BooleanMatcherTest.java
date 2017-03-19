package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.BooleanColumn;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BooleanMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        BooleanColumn c = csv.addBooleanColumn("visible");
        Row r = csv.addRow().setBoolean(c, Boolean.FALSE);
        BooleanMatcher m1 = new BooleanMatcher(c, Boolean.FALSE);
        BooleanMatcher m2 = new BooleanMatcher(c, Boolean.TRUE);
        BooleanMatcher m3 = new BooleanMatcher(c, null);
        assertTrue(m1.matches(r));
        assertFalse(m2.matches(r));
        assertFalse(m3.matches(r));
    }

}