package no.laukvik.csv.query;

import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.UrlColumn;
import org.junit.Assert;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UrlQueryMatcherTest {

    @Test
    public void getColumn() {
        UrlColumn c = new UrlColumn("value");
        UrlQueryMatcher m = new UrlQueryMatcher(c, "");
        Assert.assertEquals(c, m.getColumn());
    }

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