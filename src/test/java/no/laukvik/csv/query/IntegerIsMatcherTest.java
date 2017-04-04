package no.laukvik.csv.query;

import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.IntegerColumn;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 */
public class IntegerIsMatcherTest {

    @Test
    public void getColumn() {
        IntegerColumn c = new IntegerColumn("value");
        IntegerIsInMatcher m = new IntegerIsInMatcher(c, 2);
        assertEquals(c, m.getColumn());
    }


    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        IntegerColumn created = csv.addIntegerColumn("created");
        IntegerIsInMatcher m = new IntegerIsInMatcher(created, 3);
        assertTrue(m.matches(3));
        assertFalse(m.matches(4));
        assertFalse(m.matches(5));
        assertFalse(m.matches(null));
    }

}