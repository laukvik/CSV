package no.laukvik.csv.query;

import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.IntegerColumn;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 */
public class IntegerLessThanMatcherTest {

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        IntegerColumn created = csv.addIntegerColumn("created");
        IntegerLessThanMatcher m = new IntegerLessThanMatcher(created, 4);
        assertTrue(m.matches(3));
        assertFalse(m.matches(4));
        assertFalse(m.matches(5));
        assertFalse(m.matches(null));
    }

}