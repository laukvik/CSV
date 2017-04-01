package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.IntegerColumn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 */
public class IntegerIsMatcherTest {

    @Test
    public void getColumn() {
        IntegerColumn c = new IntegerColumn("value");
        IntegerIsMatcher m = new IntegerIsMatcher(c, 2);
        assertEquals(c, m.getColumn());
    }


    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        IntegerColumn created = csv.addIntegerColumn("created");
        IntegerIsMatcher m = new IntegerIsMatcher(created, 3);
        assertTrue( m.matches(3) );
        assertFalse( m.matches(4) );
        assertFalse( m.matches(5) );
        assertFalse( m.matches(null) );
    }

}