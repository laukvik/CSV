package org.laukvik.csv.query;

import org.junit.Before;
import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.UrlColumn;

import java.net.URL;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UrlFilePrefixMatcherTest {

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
    public void matches() {
        UrlFilePrefixMatcher m = new UrlFilePrefixMatcher(c, "hello");
        assertFalse(m.matches(r1));
        assertFalse(m.matches(r2));
        assertTrue(m.matches(r3));
        assertTrue(m.matches(r4));
        assertTrue(m.matches(r5));
        assertTrue(m.matches(r6));
        assertFalse(m.matches(r7));
    }

    @Test
    public void matchesNulls(){
        String s = null;
        UrlFilePrefixMatcher m = new UrlFilePrefixMatcher(c, s);
        assertTrue(m.matches(r1));
        assertTrue(m.matches(r7));
    }

}