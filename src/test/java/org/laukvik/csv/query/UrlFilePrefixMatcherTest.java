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

public class UrlFilePrefixMatcherTest {

    CSV csv;
    UrlColumn c;

    @Before
    public void before() throws Exception{
        csv = new CSV();
        c = csv.addUrlColumn("value");
    }

    @Test
    public void getColumn() {
        UrlColumn c = new UrlColumn("value");
        UrlFilePrefixMatcher m = new UrlFilePrefixMatcher(c, "");
        assertEquals(c, m.getColumn());
    }

    @Test
    public void matches() throws MalformedURLException {
        UrlFilePrefixMatcher m = new UrlFilePrefixMatcher(c, "hello");
        assertFalse(m.matches(new URL("http://localhost")));
        assertFalse(m.matches(new URL("http://localhost/")));
        assertTrue(m.matches(new URL("http://localhost/hello")));
        assertTrue(m.matches(new URL("http://localhost/hello.jpg")));
        assertTrue(m.matches(new URL("http://localhost/img/hello")));
        assertTrue(m.matches(new URL("http://localhost/img/hello.jpg")));
        assertFalse(m.matches(null));
    }

    @Test
    public void matchesNulls() throws MalformedURLException {
        String s = null;
        UrlFilePrefixMatcher m = new UrlFilePrefixMatcher(c, s);
        assertTrue(m.matches(new URL("http://localhost")));
        assertTrue(m.matches(null));
    }

}