package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
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
        Row r1 = csv.addRow().setString(c, "Bill");
        Row r2 = csv.addRow().setString(c, "Bal l");
        RegExMatcher m = new RegExMatcher(c, "^(.*)ll");
        assertTrue( m.matches(r1));
        assertFalse( m.matches(r2));
    }

}