package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.StringColumn;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 */
public class NotEmptyMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("first");
        Row r1 = csv.addRow().setString(first, "Steve");
        Row r2 = csv.addRow();
        NotEmptyMatcher m = new NotEmptyMatcher(first);
        assertTrue(m.matches(r1));
        assertFalse(m.matches(r2));
    }

}