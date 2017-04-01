package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.columns.StringColumn;

import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RegExMatcherTest {

    @Test
    public void getColumn() {
        StringColumn c = new StringColumn("first");
        RegExMatcher m = new RegExMatcher(c, Pattern.compile("^(.*)ll"));
        assertEquals(c, m.getColumn());
    }

    @Test
    public void matches() throws Exception {
        StringColumn c = new StringColumn("first");
        Pattern p = Pattern.compile("^(.*)ll");
        RegExMatcher m = new RegExMatcher(c, p);
        assertTrue( m.matches("Bill"));
        assertFalse( m.matches("Bal l"));
    }

}