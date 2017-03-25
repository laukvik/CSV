package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.StringColumn;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PostfixMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("filename");
        Row r1 = csv.addRow().setString(first, "thumb-01.jpg");
        Row r2 = csv.addRow().setString(first, "thumb-02.jpg");
        Row r3 = csv.addRow().setString(first, "thumb-01.gif");
        Row r4 = csv.addRow();
        PostfixMatcher m = new PostfixMatcher(first, "jpg");
        assertTrue(m.matches(r1));
        assertTrue(m.matches(r2));
        assertFalse(m.matches(r3));
        assertFalse(m.matches(r4));
    }

}