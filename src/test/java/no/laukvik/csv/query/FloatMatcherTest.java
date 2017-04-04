package no.laukvik.csv.query;

import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.FloatColumn;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FloatMatcherTest {

    @Test
    public void getColumn() {
        FloatColumn c = new FloatColumn("value");
        FloatMatcher m = new FloatMatcher(c);
        assertEquals(c, m.getColumn());
    }

    @Test
    public void matches() throws Exception {
        CSV csv = new CSV();
        FloatColumn c = csv.addFloatColumn("value");

        FloatMatcher m = new FloatMatcher(c, 20f);
        assertTrue(m.matches(20f));
        assertFalse(m.matches(30f));
        assertFalse(m.matches(40f));
        assertFalse(m.matches(null));
    }

}