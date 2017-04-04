package no.laukvik.csv.query;

import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.UrlColumn;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UrlMatcherTest {

    @Test
    public void getColumn() throws MalformedURLException {
        UrlColumn c = new UrlColumn("value");
        UrlMatcher m = new UrlMatcher(c, new URL("http://localhost"));
        assertEquals(c, m.getColumn());
    }

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        UrlColumn c = csv.addUrlColumn("value");
        UrlMatcher m = new UrlMatcher(c, new URL("http://localhost"));
        assertTrue(m.matches(new URL("http://localhost")));
        assertFalse(m.matches(new URL("http://wikipedia.org/")));
        assertFalse(m.matches(null));
    }

}