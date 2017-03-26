package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.UrlColumn;

import java.net.URL;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UrlQueryMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        UrlColumn c = csv.addUrlColumn("value");
        UrlQueryMatcher m = new UrlQueryMatcher(c, "first=morten");
        assertFalse(m.matches(new URL("http://localhost")));
        assertFalse(m.matches(new URL("http://localhost/?")));
        assertFalse(m.matches(new URL("http://localhost/?first")));
        assertTrue(m.matches(new URL("http://localhost/?first=morten")));
        assertFalse(m.matches(new URL("http://localhost/")));
        assertFalse(m.matches(null));
    }

}