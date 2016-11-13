package org.laukvik.csv.columns;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 */
public class DoubleColumnTest {

    @Test
    public void asString() throws Exception {
        DoubleColumn dc = new DoubleColumn("quantity");
        assertEquals("quantity", dc.getName());
        assertEquals("512.3", dc.asString(512.3d));
        assertEquals("", dc.asString(null));
    }

    @Test
    public void parse() throws Exception {
        DoubleColumn dc = new DoubleColumn("quantity");
        assertEquals( (Double)512.3, dc.parse("512.3"));
        assertEquals(null, dc.parse(""));
        assertEquals(null, dc.parse(null));
    }

    @Test
    public void compare() throws Exception {
        DoubleColumn dc = new DoubleColumn("quantity");
        assertEquals(-1, dc.compare( 5d, 10d ));
        assertEquals(0, dc.compare( 5d, 5d ));
        assertEquals(1, dc.compare( 10d, 5d ));
        assertEquals(1, dc.compare( 10d, null ));
        assertEquals(-1, dc.compare( null, 5d ));
    }

}