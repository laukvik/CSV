package no.laukvik.csv.query;

import no.laukvik.csv.columns.StringColumn;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 */
public class EmptyMatcherTest {

    @Test
    public void getColumn() {
        StringColumn c = new StringColumn("value");
        EmptyMatcher m = new EmptyMatcher(c);
        assertEquals(c, m.getColumn());
    }

    @Test
    public void matches() throws Exception {
        StringColumn first = new StringColumn("first");
        EmptyMatcher m = new EmptyMatcher(first);
        assertFalse(m.matches("Steve"));
        assertTrue(m.matches(null));
    }

}