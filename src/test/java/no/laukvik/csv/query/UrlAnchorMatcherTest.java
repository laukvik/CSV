package no.laukvik.csv.query;

import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.UrlColumn;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UrlAnchorMatcherTest {

    @Test
    public void getColumn() {
        UrlColumn c = new UrlColumn("first");
        UrlAnchorMatcher m = new UrlAnchorMatcher(c, "");
        assertEquals(c, m.getColumn());
    }

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        UrlColumn c = csv.addUrlColumn("value");
        UrlAnchorMatcher m = new UrlAnchorMatcher(c, "hello");
        assertFalse(m.matches(new URL("http://localhost/")));
        assertFalse(m.matches(new URL("http://localhost/#")));
        assertFalse(m.matches(new URL("http://localhost/#a")));
        assertFalse(m.matches(new URL("http://localhost/##")));
        assertFalse(m.matches(null));

        String s = null;
        UrlAnchorMatcher m2 = new UrlAnchorMatcher(c, s);
        assertTrue(m2.matches(null));
    }

}