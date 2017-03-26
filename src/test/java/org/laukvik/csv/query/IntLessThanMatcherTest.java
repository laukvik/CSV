package org.laukvik.csv.query;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.IntegerColumn;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 */
public class IntLessThanMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        IntegerColumn created = csv.addIntegerColumn("created");
        IntLessThanMatcher m = new IntLessThanMatcher(created, 4);
        assertTrue( m.matches(3) );
        assertFalse( m.matches(4) );
        assertFalse( m.matches(5) );
        assertFalse( m.matches(null) );
    }

}