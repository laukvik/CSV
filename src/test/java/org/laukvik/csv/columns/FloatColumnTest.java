package org.laukvik.csv.columns;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 */
public class FloatColumnTest {

    @Test
    public void asString() throws Exception {
        FloatColumn dc = new FloatColumn("quantity");
        assertEquals("quantity", dc.getName());
        assertEquals("512.3", dc.asString(512.3f));
        assertEquals("", dc.asString(null));
    }

    @Test
    public void parse() throws Exception {
        FloatColumn dc = new FloatColumn("quantity");
        assertEquals( (Float)512.3f,  dc.parse("512.3"));
        assertEquals( null, dc.parse(""));
        assertEquals( null, dc.parse(null));
    }

    @Test
    public void compare() throws Exception {
        FloatColumn dc = new FloatColumn("quantity");
        assertEquals(-1, dc.compare( 5f, 10f ));
        assertEquals(0, dc.compare( 5f, 5f ));
        assertEquals(1, dc.compare( 10f, 5f ));
        assertEquals(1, dc.compare( 10f, null ));
        assertEquals(-1, dc.compare( null, 5f ));
    }

}