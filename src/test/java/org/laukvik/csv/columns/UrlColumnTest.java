package org.laukvik.csv.columns;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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

    @Test
    public void getFilename() throws MalformedURLException {
        URL u1 = new URL("http://en.wikipedia.org/wiki/Barack_Obama.html");
        URL u2 = new URL("http://en.wikipedia.org");
        URL u3 = new URL("http://en.wikipedia.org/wiki/Barack_Obama");
        URL u4 = new URL("http://en.wikipedia.org/Barack_Obama");
        URL u5 = new URL("http://en.wikipedia.org/Barack_Obama.html");
        assertEquals("Barack_Obama.html", UrlColumn.getFilename(u1));
        assertEquals(null, UrlColumn.getFilename(u2));
        assertEquals("Barack_Obama", UrlColumn.getFilename(u3));
        assertEquals("Barack_Obama", UrlColumn.getFilename(u4));
        assertEquals("Barack_Obama.html", UrlColumn.getFilename(u5));
        assertEquals(null, UrlColumn.getFilename(null));
    }

    @Test
    public void getPrefix() throws MalformedURLException {
        URL u1 = new URL("http://en.wikipedia.org/wiki/Barack_Obama.html");
        URL u2 = new URL("http://en.wikipedia.org");
        URL u3 = new URL("http://en.wikipedia.org/wiki/Barack_Obama");
        URL u4 = new URL("http://en.wikipedia.org/Barack_Obama");
        URL u5 = new URL("http://en.wikipedia.org/Barack_Obama.html");
        assertEquals("Barack_Obama", UrlColumn.getPrefix(u1));
        assertEquals(null, UrlColumn.getPrefix(u2));
        assertEquals("Barack_Obama", UrlColumn.getPrefix(u3));
        assertEquals("Barack_Obama", UrlColumn.getPrefix(u4));
        assertEquals("Barack_Obama", UrlColumn.getPrefix(u5));
        assertEquals(null, UrlColumn.getPrefix(null));
    }

    @Test
    public void getPostfix() throws MalformedURLException {
        URL u1 = new URL("http://en.wikipedia.org/wiki/Barack_Obama.html");
        URL u2 = new URL("http://en.wikipedia.org");
        URL u3 = new URL("http://en.wikipedia.org/wiki/Barack_Obama");
        URL u4 = new URL("http://en.wikipedia.org/Barack_Obama");
        URL u5 = new URL("http://en.wikipedia.org/Barack_Obama.html");
        assertEquals("html", UrlColumn.getPostfix(u1));
        assertEquals(null, UrlColumn.getPostfix(u2));
        assertEquals(null, UrlColumn.getPostfix(u3));
        assertEquals(null, UrlColumn.getPostfix(u4));
        assertEquals("html", UrlColumn.getPostfix(u5));
        assertEquals(null, UrlColumn.getPostfix(null));
    }

    @Test
    public void getAnchor() throws MalformedURLException {
        URL u1 = new URL("http://en.wikipedia.org/wiki/Barack_Obama#history");
        URL u2 = new URL("http://en.wikipedia.org/wiki/Barack_Obama");
        assertEquals("history", UrlColumn.getAnchor(u1));
        assertEquals(null, UrlColumn.getAnchor(u2));
        assertEquals(null, UrlColumn.getAnchor(null));
    }

    @Test
    public void getPath() throws MalformedURLException {
        URL u1 = new URL("http://en.wikipedia.org/wiki/Barack_Obama.jpg");
        assertEquals("/wiki/Barack_Obama.jpg", UrlColumn.getPath(u1));
        assertEquals("/", UrlColumn.getPath(new URL("http://en.wikipedia.org/")));
        assertNull(UrlColumn.getPath(new URL("http://en.wikipedia.org")));
        assertNull(UrlColumn.getPath(null));
    }

    @Test
    public void getPort() throws MalformedURLException {
        URL u1 = new URL("http://en.wikipedia.org:8080/wiki/Barack_Obama.jpg");
        URL u2 = new URL("http://en.wikipedia.org/wiki/Barack_Obama.jpg");
        assertEquals((Integer) 8080, UrlColumn.getPort(u1));
        assertNull(UrlColumn.getPort(null));
        assertNull(UrlColumn.getPort(u2));
    }

    @Test
    public void getProtocol() throws MalformedURLException {
        URL u1 = new URL("http://en.wikipedia.org/wiki/Barack_Obama.jpg");
        assertEquals("http", UrlColumn.getProtocol(u1));
        assertNull(UrlColumn.getProtocol(null));
    }

}