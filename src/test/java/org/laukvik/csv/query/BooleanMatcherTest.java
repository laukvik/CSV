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

        Row r1 = csv.addRow().setBoolean(c, Boolean.FALSE);
        Row r2 = csv.addRow().setBoolean(c, Boolean.TRUE);
        Row r3 = csv.addRow();
        BooleanMatcher m1 = new BooleanMatcher(c, Boolean.FALSE);
        BooleanMatcher m2 = new BooleanMatcher(c, Boolean.TRUE);
        Boolean b = null;
        BooleanMatcher m3 = new BooleanMatcher(c, b);

        assertTrue(m1.matches(r1));
        assertFalse(m2.matches(r1));
        assertFalse(m3.matches(r1));

        assertFalse(m1.matches(r2));
        assertTrue(m2.matches(r2));
        assertFalse(m3.matches(r2));

        assertFalse(m1.matches(r3));
        assertFalse(m2.matches(r3));
        assertTrue(m3.matches(r3));
    }

}