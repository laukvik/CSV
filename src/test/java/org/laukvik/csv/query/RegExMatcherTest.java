package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.StringColumn;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 */
public class RegExMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        StringColumn c = new StringColumn("first");
        csv.addColumn(c);
        RegExMatcher m = new RegExMatcher(c, "^(.*)ll");
        assertTrue( m.matches("Bill"));
        assertFalse( m.matches("Bal l"));
    }

}