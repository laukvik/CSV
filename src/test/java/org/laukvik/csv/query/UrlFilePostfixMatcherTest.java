package org.laukvik.csv.query;

import org.junit.Before;
import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.UrlColumn;

import java.net.URL;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UrlFilePostfixMatcherTest {

    CSV csv;
    UrlColumn c;
    Row r1, r2, r3, r4, r5, r6, r7;

    @Before
    public void before() throws Exception{
        csv = new CSV();
        c = csv.addUrlColumn("value");
        r1 = csv.addRow().setURL(c, new URL("http://localhost"));
        r2 = csv.addRow().setURL(c, new URL("http://localhost/"));
        r3 = csv.addRow().setURL(c, new URL("http://localhost/hello"));
        r4 = csv.addRow().setURL(c, new URL("http://localhost/hello.jpg"));
        r5 = csv.addRow().setURL(c, new URL("http://localhost/img/hello"));
        r6 = csv.addRow().setURL(c, new URL("http://localhost/img/hello.jpg"));
        r7 = csv.addRow();
    }

    @Test
    public void matches() throws Exception {
        UrlFilePostfixMatcher m = new UrlFilePostfixMatcher(c, "jpg");
        assertFalse(m.matches(r1));
        assertFalse(m.matches(r2));
        assertFalse(m.matches(r3));
        assertTrue(m.matches(r4));
        assertFalse(m.matches(r5));
        assertTrue(m.matches(r6));
        assertFalse(m.matches(r7));
    }

    @Test
    public void matchesNull() {
        String s = null;
        UrlFilePostfixMatcher m = new UrlFilePostfixMatcher(c, s);
        assertTrue(m.matches(r7));
        assertTrue(m.matches(r1));
    }

    @Test
    public void matchesSpace() {
        String s = "";
        UrlFilePostfixMatcher m = new UrlFilePostfixMatcher(c, s);
        assertFalse(m.matches(r7));
        assertFalse(m.matches(r1));
    }

}