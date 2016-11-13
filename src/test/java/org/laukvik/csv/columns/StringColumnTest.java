package org.laukvik.csv.columns;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 */
public class StringColumnTest {

    @Test
    public void getSize() throws Exception {
        StringColumn c = new StringColumn("hospital");
        c.setSize(10);
        assertEquals(10, c.getSize());
        assertEquals("hospital", c.getName());
    }

    @Test
    public void asString() throws Exception {
        StringColumn c = new StringColumn("hospital");
        assertEquals("Fake road 12", c.asString("Fake road 12"));
        assertEquals("", c.asString(null));
    }

    @Test
    public void parse() throws Exception {
        StringColumn c = new StringColumn("hospital");
        assertEquals("Fake road 12", c.parse("Fake road 12"));
        assertEquals("", c.parse(null));
    }

    @Test
    public void compare() throws Exception {

    }

}