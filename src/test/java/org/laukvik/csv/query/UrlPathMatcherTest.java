package org.laukvik.csv.query;

import org.junit.Before;
import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.UrlColumn;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UrlPathMatcherTest {

    CSV csv;
    UrlColumn c;

    @Before
    public void before() throws MalformedURLException {
        csv = new CSV();
        c = csv.addUrlColumn("value");
    }

    @Test
    public void getColumn() {
        UrlColumn c = new UrlColumn("value");
        UrlPathMatcher m = new UrlPathMatcher(c, "");
        assertEquals(c, m.getColumn());
    }

    @Test
    public void matches() throws MalformedURLException {
        UrlPathMatcher m = new UrlPathMatcher(c, "/img");
        assertFalse(m.matches(new URL("http://localhost")));
        assertTrue(m.matches(new URL("http://localhost/img")));
        assertFalse(m.matches(new URL("http://localhost/img/")));
        assertFalse(m.matches(new URL("http://localhost/##")));
        assertFalse(m.matches(null));
    }

    @Test
    public void matchesNull() throws MalformedURLException {
        String s2 = null;
        UrlPathMatcher m = new UrlPathMatcher(c, s2);
        assertTrue(m.matches(new URL("http://localhost")));
        assertFalse(m.matches(new URL("http://localhost/img")));
        assertFalse(m.matches(new URL("http://localhost/img/")));
        assertFalse(m.matches(new URL("http://localhost/##")));
        assertTrue(m.matches(null));
    }

}