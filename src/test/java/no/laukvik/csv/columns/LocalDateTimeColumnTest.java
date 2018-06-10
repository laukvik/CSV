package no.laukvik.csv.columns;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class LocalDateTimeColumnTest {

    @Test
    public void asString() throws Exception {
        LocalDateTimeColumn c = new LocalDateTimeColumn("localDateTime");
        assertEquals("localDateTime", c.getName());
        assertEquals("2017-12-23T23:23:59", c.asString(LocalDateTime.parse("2017-12-23T23:23:59")));
        assertEquals("", c.asString(null));
    }

    @Test
    public void parse() throws Exception {
        LocalDateTimeColumn dc = new LocalDateTimeColumn("localDateTime");
        assertEquals(LocalDateTime.parse("2017-12-23T23:23:59"), dc.parse("2017-12-23T23:23:59"));
        assertEquals(null, dc.parse(""));
        assertEquals(null, dc.parse(null));
    }

    @Test
    public void compare() throws Exception {
        LocalDateTimeColumn dc = new LocalDateTimeColumn("localDateTime");
        assertEquals(-1, dc.compare(LocalDateTime.parse("2017-12-22T23:23:59"), LocalDateTime.parse("2017-12-23T23:23:59")));
        assertEquals(0, dc.compare(LocalDateTime.parse("2017-12-23T23:23:59"), LocalDateTime.parse("2017-12-23T23:23:59")));
        assertEquals(1, dc.compare(LocalDateTime.parse("2017-12-24T23:23:59"), LocalDateTime.parse("2017-12-23T23:23:59")));
    }
}