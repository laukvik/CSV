package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.UrlColumn;

import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UrlFileMatcherTest {

    @Test
    public void getColumn() {
        UrlColumn c = new UrlColumn("value");
        UrlFileMatcher m = new UrlFileMatcher(c, "");
        assertEquals(c, m.getColumn());
    }

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        UrlColumn c = csv.addUrlColumn("value");
        UrlFileMatcher m = new UrlFileMatcher(c, "hello.jpg");
        assertFalse(m.matches(new URL("http://localhost")));
        assertFalse(m.matches(new URL("http://localhost/")));
        assertFalse(m.matches(new URL("http://localhost/hello")));
        assertTrue(m.matches(new URL("http://localhost/hello.jpg")));
        assertFalse(m.matches(new URL("http://localhost/img/hello")));
        assertTrue(m.matches(new URL("http://localhost/img/hello.jpg")));
    }

}