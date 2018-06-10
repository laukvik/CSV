package no.laukvik.csv.columns;

import org.junit.Test;

import java.time.LocalTime;

import static org.junit.Assert.assertEquals;

public class LocalTimeColumnTest {

    @Test
    public void asString() throws Exception {
        LocalTimeColumn c = new LocalTimeColumn("LocalTime");
        assertEquals("LocalTime", c.getName());
        assertEquals("23:59:59", c.asString(LocalTime.parse("23:59:59")));
        assertEquals("", c.asString(null));
    }

    @Test
    public void parse() throws Exception {
        LocalTimeColumn dc = new LocalTimeColumn("LocalTime");
        assertEquals(LocalTime.parse("23:59:59"), dc.parse("23:59:59"));
        assertEquals(null, dc.parse(""));
        assertEquals(null, dc.parse(null));
    }

    @Test
    public void compare() throws Exception {
        LocalTimeColumn dc = new LocalTimeColumn("LocalTime");
        assertEquals(-1, dc.compare(LocalTime.parse("23:59:58"), LocalTime.parse("23:59:59")));
        assertEquals(0, dc.compare(LocalTime.parse("23:59:59"), LocalTime.parse("23:59:59")));
        assertEquals(1, dc.compare(LocalTime.parse("23:59:59"), LocalTime.parse("23:59:58")));
    }
}