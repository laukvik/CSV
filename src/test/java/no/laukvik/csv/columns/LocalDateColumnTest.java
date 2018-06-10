package no.laukvik.csv.columns;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class LocalDateColumnTest {

    @Test
    public void asString() throws Exception {
        LocalDateColumn c = new LocalDateColumn("localDate");
        assertEquals("localDate", c.getName());
        assertEquals("2017-12-23", c.asString(LocalDate.parse("2017-12-23")));
        assertEquals("", c.asString(null));
    }

    @Test
    public void parse() throws Exception {
        LocalDateColumn dc = new LocalDateColumn("localDate");
        assertEquals(LocalDate.parse("2017-12-23"), dc.parse("2017-12-23"));
        assertEquals(null, dc.parse(""));
        assertEquals(null, dc.parse(null));
    }

    @Test
    public void compare() throws Exception {
        LocalDateColumn dc = new LocalDateColumn("localDate");
        assertEquals(-1, dc.compare(LocalDate.parse("2017-12-22"), LocalDate.parse("2017-12-23")));
        assertEquals(0, dc.compare(LocalDate.parse("2017-12-23"), LocalDate.parse("2017-12-23")));
        assertEquals(1, dc.compare(LocalDate.parse("2017-12-24"), LocalDate.parse("2017-12-23")));
    }

}