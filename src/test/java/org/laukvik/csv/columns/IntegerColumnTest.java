package org.laukvik.csv.columns;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Morten Laukvik
 */
public class IntegerColumnTest {

    @Test
    public void asString() throws Exception {
        IntegerColumn dc = new IntegerColumn("quantity");
        assertEquals("quantity", dc.getName());
        assertEquals("512", dc.asString(512));
        assertEquals("", dc.asString(null));
    }

    @Test
    public void parse() throws Exception {
        IntegerColumn dc = new IntegerColumn("quantity");
        assertEquals( (Integer) 512, dc.parse("512"));
        assertEquals( null, dc.parse(""));
        assertEquals( null, dc.parse(null));
    }

    @Test
    public void compare() throws Exception {
        IntegerColumn dc = new IntegerColumn("quantity");
        assertEquals(-1, dc.compare( 5, 10 ));
        assertEquals(0, dc.compare( 5, 5 ));
        assertEquals(1, dc.compare( 10, 5 ));
        assertEquals(1, dc.compare( 10, null ));
        assertEquals(-1, dc.compare( null, 5 ));
        assertEquals(0, dc.compare( null, null ));
    }

}