package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.StringColumn;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PrefixMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("jpg");
        Row r1 = csv.addRow().setString(first, "thumb-01.jpg");
        Row r2 = csv.addRow().setString(first, "thumb-02.jpg");
        Row r3 = csv.addRow().setString(first, "thumb-01.gif");
        Row r4 = csv.addRow();
        PrefixMatcher m = new PrefixMatcher(first, "thumb-01");
        assertTrue(m.matches(r1));
        assertFalse(m.matches(r2));
        assertTrue(m.matches(r3));
        assertFalse(m.matches(r4));
    }

}