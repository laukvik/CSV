package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.UrlColumn;

import java.net.URL;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UrlAnchorMatcherTest {

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