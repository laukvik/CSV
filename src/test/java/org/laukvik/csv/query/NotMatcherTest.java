package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.StringColumn;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 */
public class NotMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        StringColumn c1 = new StringColumn("first");

        Row r1 = csv.addRow().setString(c1, "Steve");
        Row r2 = csv.addRow().setString(c1, "Bill");

        RegExMatcher m1 = new RegExMatcher(c1, "Steve");

        NotMatcher m = new NotMatcher(m1);

        assertFalse(m.matches(r1));
        assertTrue(m.matches(r2));
    }

}