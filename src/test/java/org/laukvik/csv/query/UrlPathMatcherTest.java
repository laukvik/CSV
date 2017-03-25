package org.laukvik.csv.query;

import org.junit.Before;
import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.UrlColumn;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UrlPathMatcherTest {

    CSV csv;
    UrlColumn c;
    Row r1, r2, r3, r4, r5;

    @Before
    public void before() throws MalformedURLException {
        csv = new CSV();
        c = csv.addUrlColumn("value");
        r1 = csv.addRow().setURL(c, new URL("http://localhost"));
        r2 = csv.addRow().setURL(c, new URL("http://localhost/img"));
        r3 = csv.addRow().setURL(c, new URL("http://localhost/img/"));
        r4 = csv.addRow().setURL(c, new URL("http://localhost/##"));
        r5 = csv.addRow().setURL(c, null);
    }

    @Test
    public void matches() {
        UrlPathMatcher m = new UrlPathMatcher(c, "/img");
        assertFalse(m.matches(r1));
        assertTrue(m.matches(r2));
        assertFalse(m.matches(r3));
        assertFalse(m.matches(r4));
        assertFalse(m.matches(r5));
    }

    @Test
    public void matchesNull(){
        String s2 = null;
        UrlPathMatcher m = new UrlPathMatcher(c, s2);
        assertTrue(m.matches(r1));
        assertFalse(m.matches(r2));
        assertFalse(m.matches(r3));
        assertFalse(m.matches(r4));
        assertTrue(m.matches(r5));
    }

}