package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.IntegerColumn;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 */
public class IntIsMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        IntegerColumn created = csv.addIntegerColumn("created");
        Row r1 = csv.addRow().setInteger(created, 3);
        Row r2 = csv.addRow().setInteger(created, 4);
        Row r3 = csv.addRow().setInteger(created, 5);
        Row r4 = csv.addRow();
        IntIsMatcher m = new IntIsMatcher(created, 4);
        assertTrue( m.matches(r2) );
        assertFalse( m.matches(r1) );
        assertFalse( m.matches(r3) );
        assertFalse( m.matches(r4) );
    }

}