package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.UrlColumn;

import java.net.URL;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UrlHostMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        UrlColumn c = csv.addUrlColumn("value");
        UrlHostMatcher m = new UrlHostMatcher(c, "localhost");
        assertTrue(m.matches(new URL("http://localhost")));
        assertFalse(m.matches(new URL("http://wikipedia.org/")));
        assertFalse(m.matches(null));

        String s2 = null;
        UrlHostMatcher m2 = new UrlHostMatcher(c, s2);
        assertFalse(m2.matches(new URL("http://localhost")));
        assertFalse(m2.matches(new URL("http://wikipedia.org/")));
        assertTrue(m2.matches(null));
    }

}