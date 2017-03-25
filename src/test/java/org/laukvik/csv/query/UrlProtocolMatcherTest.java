package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.columns.UrlColumn;

import java.net.URL;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UrlProtocolMatcherTest {

    @Test
    public void matches() throws Exception {
        URL u1 = new URL("http://en.wikipedia.org:8080/wiki/Barack_Obama#history");
        URL u2 = new URL("https://en.wikipedia.org:80/wiki/Barack_Obama#history");
        URL u3 = new URL("ftp://en.wikipedia.org/wiki/Barack_Obama#history");
        URL u4 = null;
        UrlColumn c = new UrlColumn("url");

        UrlProtocolMatcher m = new UrlProtocolMatcher(c, "http");
        assertTrue(m.matches(u1));
        assertFalse(m.matches(u2));
        assertFalse(m.matches(u3));
        assertFalse(m.matches(u4));

        String s2 = null;
        UrlProtocolMatcher m2 = new UrlProtocolMatcher(c, s2);
        assertFalse(m2.matches(u1));
        assertFalse(m2.matches(u2));
        assertFalse(m2.matches(u3));
        assertTrue(m2.matches(u4));
    }

}