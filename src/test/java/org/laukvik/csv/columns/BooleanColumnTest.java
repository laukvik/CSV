package org.laukvik.csv.columns;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Morten Laukvik
 */
public class BooleanColumnTest {

    @Test
    public void asString() throws Exception {
        BooleanColumn c = new BooleanColumn("finished");
        assertEquals("true", c.asString(Boolean.TRUE));
        assertEquals("false", c.asString(Boolean.FALSE));
        assertEquals("", c.asString(null));
    }

    @Test
    public void parse() throws Exception {
        BooleanColumn c = new BooleanColumn("finished");
        assertEquals(Boolean.TRUE, c.parse("true"));
        assertEquals(Boolean.FALSE, c.parse("false"));
        assertNull(c.parse(""));
        assertNull(c.parse(null));
    }

    @Test
    public void compare() throws Exception {
        BooleanColumn c = new BooleanColumn("finished");
        assertEquals(0, c.compare(Boolean.TRUE, Boolean.TRUE));
        assertEquals(1, c.compare(Boolean.TRUE, Boolean.FALSE));
        assertEquals(0, c.compare(Boolean.FALSE, Boolean.FALSE));
        assertEquals(-1, c.compare(Boolean.TRUE, null));
        assertEquals(1, c.compare(null, Boolean.FALSE));
        assertEquals(0, c.compare(null, null));
    }

}