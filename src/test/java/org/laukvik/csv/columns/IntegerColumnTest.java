package org.laukvik.csv.columns;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void isOdd() throws Exception {
        assertFalse( IntegerColumn.isOdd(null));
        assertTrue( IntegerColumn.isOdd(5));
        assertFalse( IntegerColumn.isOdd(4));
    }

    @Test
    public void isNegative() throws Exception {
        assertFalse( IntegerColumn.isNegative(null));
        assertFalse( IntegerColumn.isNegative(0));
        assertTrue( IntegerColumn.isNegative(-1));
        assertFalse( IntegerColumn.isNegative(1));
    }

    @Test
    public void getTen() throws Exception {
        assertEquals( (Integer) 0, IntegerColumn.getTen(null));
        assertEquals( (Integer) 20, IntegerColumn.getTen(20));
        assertEquals( (Integer) 30, IntegerColumn.getTen(33));
        assertEquals( (Integer) 40, IntegerColumn.getTen(444));
    }

    @Test
    public void getHundred() throws Exception {
        assertEquals( (Integer) 0, IntegerColumn.getHundred(null));
        assertEquals( (Integer) 200, IntegerColumn.getHundred(200));
        assertEquals( (Integer) 300, IntegerColumn.getHundred(330));
        assertEquals( (Integer) 400, IntegerColumn.getHundred(4444));
    }

    @Test
    public void getThousand() throws Exception {
        assertEquals( (Integer) 0,   IntegerColumn.getThousand(null));
        assertEquals( (Integer) 2000, IntegerColumn.getThousand(2000));
        assertEquals( (Integer) 3000, IntegerColumn.getThousand(3300));
        assertEquals( (Integer) 4000, IntegerColumn.getThousand(44444));
    }

    @Test
    public void getMillion() throws Exception {
        assertEquals( (Integer) 0,   IntegerColumn.getMillion(null));
        assertEquals( (Integer) 2000000, IntegerColumn.getMillion(2000000));
        assertEquals( (Integer) 3000000, IntegerColumn.getMillion(3300000));
        assertEquals( (Integer) 4000000, IntegerColumn.getMillion(44444000));
    }

    @Test
    public void getBillion() throws Exception {
        assertEquals( (Integer) 0,   IntegerColumn.getBillion(null));
        assertEquals( (Integer) 2000000000, IntegerColumn.getBillion(2000000000));
    }

}