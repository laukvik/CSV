package no.laukvik.csv.query;

import no.laukvik.csv.CSV;
import no.laukvik.csv.Row;
import no.laukvik.csv.columns.IntegerColumn;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 */
public class IntegerBetweenMatcherTest {

    @Test
    public void isBetween() throws Exception {
        assertTrue(IntegerBetweenMatcher.isBetween(5, 0, 10));
        assertTrue(IntegerBetweenMatcher.isBetween(0, 0, 10));
        assertTrue(IntegerBetweenMatcher.isBetween(10, 0, 10));
        assertFalse(IntegerBetweenMatcher.isBetween(-1, 0, 10));
        assertFalse(IntegerBetweenMatcher.isBetween(11, 0, 10));
    }

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        IntegerColumn first = csv.addIntegerColumn("first");
        Row r1 = csv.addRow().set(first, 1);
        Row r2 = csv.addRow().set(first, 3);
        Row r3 = csv.addRow().set(first, 5);
        Row r4 = csv.addRow().set(first, 5);
        Row r5 = csv.addRow();
        IntegerBetweenMatcher m = new IntegerBetweenMatcher(first, 1, 4);
        assertTrue(m.matches(1));
        assertTrue(m.matches(3));
        assertFalse(m.matches(5));
        assertFalse(m.matches(5));
        assertFalse(m.matches(null));
    }

}