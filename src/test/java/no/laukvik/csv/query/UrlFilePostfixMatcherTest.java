package no.laukvik.csv.query;

import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.UrlColumn;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UrlFilePostfixMatcherTest {

    CSV csv;
    UrlColumn c;

    @Before
    public void before() throws Exception {
        csv = new CSV();
        c = csv.addUrlColumn("value");
    }

    @Test
    public void getColumn() {
        UrlColumn c = new UrlColumn("value");
        UrlFilePostfixMatcher m = new UrlFilePostfixMatcher(c, "");
        assertEquals(c, m.getColumn());
    }

    @Test
    public void matches() throws Exception {
        UrlFilePostfixMatcher m = new UrlFilePostfixMatcher(c, "jpg");
        assertFalse(m.matches(new URL("http://localhost")));
        assertFalse(m.matches(new URL("http://localhost/")));
        assertFalse(m.matches(new URL("http://localhost/hello")));
        assertTrue(m.matches(new URL("http://localhost/hello.jpg")));
        assertFalse(m.matches(new URL("http://localhost/img/hello")));
        assertTrue(m.matches(new URL("http://localhost/img/hello.jpg")));
        assertFalse(m.matches(null));
    }

    @Test
    public void matchesNull() throws MalformedURLException {
        String s = null;
        UrlFilePostfixMatcher m = new UrlFilePostfixMatcher(c, s);
        assertTrue(m.matches(null));
        assertTrue(m.matches(new URL("http://localhost")));
    }

    @Test
    public void matchesSpace() throws MalformedURLException {
        String s = "";
        UrlFilePostfixMatcher m = new UrlFilePostfixMatcher(c, s);
        assertFalse(m.matches(null));
        assertFalse(m.matches(new URL("http://localhost")));
    }

}