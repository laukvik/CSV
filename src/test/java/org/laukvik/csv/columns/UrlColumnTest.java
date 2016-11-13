package org.laukvik.csv.columns;

import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 */
public class UrlColumnTest {

    String google = "http://www.google.com";
    String ms = "http://www.microsoft.com";

    @Test
    public void asString() throws Exception {
        URL url = new URL(google);

        UrlColumn dc = new UrlColumn("web");
        assertEquals("web", dc.getName());
        assertEquals(google, dc.asString(url));
        assertEquals("", dc.asString(null));
    }

    @Test
    public void parse() throws Exception {
        URL url = new URL(google);

        UrlColumn dc = new UrlColumn("web");
        assertEquals( url, dc.parse(google));
        assertEquals( null, dc.parse(""));
        assertEquals( null, dc.parse(null));
    }

    @Test
    public void compare() throws Exception {
        UrlColumn dc = new UrlColumn("web");
        URL u1 = new URL(google);
        URL u2 = new URL(ms);

        assertTrue(dc.compare( u1, u2 ) < 0);
        assertTrue(dc.compare( u1, u1 ) == 0);
        assertTrue(dc.compare( u1, u2 ) < 0);
        assertTrue(dc.compare( u1, null ) > 0);
        assertTrue(dc.compare( null, u2 ) < 0);
        assertTrue(dc.compare( null, null ) == 0);

    }
    
}