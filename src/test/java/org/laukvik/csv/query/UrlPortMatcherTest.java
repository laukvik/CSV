package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.columns.UrlColumn;

import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UrlPortMatcherTest {

    @Test
    public void getColumn() {
        UrlColumn c = new UrlColumn("value");
        UrlPortMatcher m = new UrlPortMatcher(c, 80);
        assertEquals(c, m.getColumn());
    }

    @Test
    public void matches() throws Exception {
        URL u1 = new URL("http://en.wikipedia.org:8080/wiki/Barack_Obama#history");
        URL u2 = new URL("http://en.wikipedia.org:80/wiki/Barack_Obama#history");
        URL u3 = new URL("http://en.wikipedia.org/wiki/Barack_Obama#history");
        URL u4 = null;
        UrlColumn c = new UrlColumn("url");

        UrlPortMatcher m = new UrlPortMatcher(c, 8080);
        assertTrue(m.matches(u1));
        assertFalse(m.matches(u2));
        assertFalse(m.matches(u3));
        assertFalse(m.matches(u4));

        Integer i = null;
        UrlPortMatcher m2 = new UrlPortMatcher(c, i);
        assertFalse(m2.matches(u1));
        assertFalse(m2.matches(u2));
        assertTrue(m2.matches(u3));
        assertTrue(m2.matches(u4));
    }

}