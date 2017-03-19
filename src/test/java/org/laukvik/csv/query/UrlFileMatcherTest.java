package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.UrlColumn;
import java.net.URL;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UrlFileMatcherTest {

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
        UrlFileMatcher m = new UrlFileMatcher(c, "hello.jpg");
        assertFalse(m.matches(r1));
        assertFalse(m.matches(r2));
        assertFalse(m.matches(r3));
        assertTrue(m.matches(r4));
        assertFalse(m.matches(r5));
        assertTrue(m.matches(r6));
    }

}