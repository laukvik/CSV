package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.UrlColumn;

import java.net.URL;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UrlFilePrefixMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        UrlColumn c = csv.addUrlColumn("value");
        Row r1 = csv.addRow().setURL(c, new URL("http://localhost"));
        Row r2 = csv.addRow().setURL(c, new URL("http://localhost/"));
        Row r3 = csv.addRow().setURL(c, new URL("http://localhost/hello"));
        Row r4 = csv.addRow().setURL(c, new URL("http://localhost/hello.jpg"));
        Row r5 = csv.addRow().setURL(c, new URL("http://localhost/img/hello"));
        Row r6 = csv.addRow().setURL(c, new URL("http://localhost/img/hello.jpg"));
        UrlFilePrefixMatcher m = new UrlFilePrefixMatcher(c, "hello");
        assertFalse(m.matches(r1));
        assertFalse(m.matches(r2));
        assertTrue(m.matches(r3));
        assertTrue(m.matches(r4));
        assertTrue(m.matches(r5));
        assertTrue(m.matches(r6));
    }

}