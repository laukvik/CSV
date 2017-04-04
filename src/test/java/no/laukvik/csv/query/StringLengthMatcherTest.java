package no.laukvik.csv.query;

import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.StringColumn;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class StringLengthMatcherTest {

    @Test
    public void getColumn() {
        StringColumn c = new StringColumn("first");
        StringLengthMatcher m = new StringLengthMatcher(c, 2);
        assertEquals(c, m.getColumn());
    }

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("first");
        StringLengthMatcher m = new StringLengthMatcher(first, 3);
        assertTrue(m.matches("One"));
        assertTrue(m.matches("Two"));
        assertFalse(m.matches("Three"));
        assertFalse(m.matches(null));
    }

}