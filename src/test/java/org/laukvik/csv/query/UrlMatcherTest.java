package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.UrlColumn;

import java.net.URL;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UrlMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        UrlColumn c = csv.addUrlColumn("value");
        Row r1 = csv.addRow().setURL(c, new URL("http://localhost"));
        Row r2 = csv.addRow().setURL(c, new URL("http://wikipedia.org/"));
        Row r3 = csv.addRow().setURL(c, null);
        UrlMatcher m = new UrlMatcher(c, new URL("http://localhost"));
        assertTrue(m.matches(new URL("http://localhost")));
        assertFalse(m.matches(new URL("http://wikipedia.org/")));
        assertFalse(m.matches(null));
    }

}