package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.UrlColumn;
import java.net.URL;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UrlHostMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        UrlColumn c = csv.addUrlColumn("value");
        Row r1 = csv.addRow().setURL(c, new URL("http://localhost"));
        Row r2 = csv.addRow().setURL(c, new URL("http://wikipedia.org/"));
        Row r3 = csv.addRow().setURL(c, null);
        UrlHostMatcher m = new UrlHostMatcher(c, "localhost");
        assertTrue(m.matches(r1));
        assertFalse(m.matches(r2));
        assertFalse(m.matches(r3));

        String s2 = null;
        UrlHostMatcher m2 = new UrlHostMatcher(c, s2);
        assertFalse(m2.matches(r1));
        assertFalse(m2.matches(r2));
        assertTrue(m2.matches(r3));
    }

}