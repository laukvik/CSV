package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.BooleanColumn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BooleanMatcherTest {

    @Test
    public void getColumn() {
        BooleanColumn c = new BooleanColumn("value");
        BooleanMatcher m = new BooleanMatcher(c);
        assertEquals(c, m.getColumn());
    }

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        BooleanColumn c = csv.addBooleanColumn("visible");

        Row r1 = csv.addRow().set(c, Boolean.FALSE);
        Row r2 = csv.addRow().set(c, Boolean.TRUE);
        Row r3 = csv.addRow();
        BooleanMatcher m1 = new BooleanMatcher(c, Boolean.FALSE);
        BooleanMatcher m2 = new BooleanMatcher(c, Boolean.TRUE);
        Boolean b = null;
        BooleanMatcher m3 = new BooleanMatcher(c, b);

        assertTrue(m1.matches(Boolean.FALSE));
        assertFalse(m2.matches(Boolean.FALSE));
        assertFalse(m3.matches(Boolean.FALSE));

        assertFalse(m1.matches(Boolean.TRUE));
        assertTrue(m2.matches(Boolean.TRUE));
        assertFalse(m3.matches(Boolean.TRUE));

        assertFalse(m1.matches(null));
        assertFalse(m2.matches(null));
        assertTrue(m3.matches(null));
    }

}